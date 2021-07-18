package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;
import java.util.HashMap;

import static scanner.TokenKind.*;
/* <block> ::= <const decl part> <var decl part> <func decl> <proc decl> ’begin’ <statm list> ’end’ */

public class Block extends PascalSyntax {

  ConstDeclPart constDeclPart;
  VarDeclPart varDeclPart;
  ArrayList<ProcDecl> procDecls = new ArrayList<>();
  StatmList statmList;
  HashMap<String,PascalDecl> decls = new HashMap<>();
  Block outerScope = null;

  public Block (int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<block> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    if(constDeclPart!=null) {
      constDeclPart.prettyPrint();
    }
    if(varDeclPart!=null) {
      varDeclPart.prettyPrint();
    }
    for(ProcDecl decl: procDecls) {
      decl.prettyPrint();
    }
    Main.log.prettyPrintLn();
    Main.log.prettyPrintLn("begin");
    Main.log.prettyIndent();
    statmList.prettyPrint();
    Main.log.prettyOutdent();
    Main.log.prettyPrintLn();
    Main.log.prettyPrint("end");
    Main.log.prettyOutdent();
  }


  public static Block parse(Scanner s) {
    enterParser("block");
    Block block = new Block(s.curLineNum());

    if (s.curToken.kind == constToken) {
      block.constDeclPart = ConstDeclPart.parse(s);
      block.constDeclPart.context = block;
    }

    if (s.curToken.kind == varToken) {
      block.varDeclPart = VarDeclPart.parse(s);
      block.varDeclPart.context = block;
    }

    while (s.curToken.kind == functionToken || s.curToken.kind == procedureToken) {
      if (s.curToken.kind == functionToken) {
        FuncDecl funcDecl = FuncDecl.parse(s);
        funcDecl.context = block;
        block.procDecls.add(funcDecl);
      } else {
        ProcDecl procDecl = ProcDecl.parse(s);
        procDecl.context = block;
        block.procDecls.add(procDecl);
      }
    }

    s.skip(beginToken);
    block.statmList = StatmList.parse(s);
    block.statmList.context = block;
    s.skip(endToken);
    leaveParser("block");
    return block;
  }

  void addDecl(String id, PascalDecl d) {
    if(decls.containsKey(id)) {
      d.error(id + " declared twice in same block!");
    }
    decls.put(id,d);
  }

  @Override
  void check(Block curScope, Library lib) {
    outerScope = curScope; // FIXME: Line number ?
    if(constDeclPart != null) {
      constDeclPart.check(this, lib);
    }
    if(varDeclPart != null) {
      varDeclPart.check(this, lib);
    }
    for(ProcDecl d: procDecls) {
        d.check(this, lib);
    }
    statmList.check(this, lib);
  }

  PascalDecl findDecl(String id, PascalSyntax where) {
    PascalDecl d = decls.get(id);
    if (d != null) {
      Main.log.noteBinding(id, where, d);
      return d;
    }
    if (outerScope != null)
      return outerScope.findDecl(id,where);
    where.error("Name " + id + " is unknown!");
    return null; // Required by the Java compiler.
  }

  private int offset() {
    if(varDeclPart!=null) {
      return 32 + 4 * varDeclPart.varDeclParts.size();
    } else {
      return 32;
    }
  }

  @Override
  void genCode(CodeFile f) {
    f.level++;

    // FIXME: Can I avoid casting ?
    PascalDecl decl = (PascalDecl) context;
    decl.label = decl.progProcFuncName + "$" + f.getLabel(decl.name);

    for(ProcDecl d: procDecls) {
      d.genCode(f);
    }
    f.genInstr(decl.label, "", "", "");
    f.genInstr("", "enter", String.format("$%d,$%d", offset(), decl.declLevel), "Start of " + decl.name);

    if(constDeclPart!=null) {
      constDeclPart.genCode(f);
    }
    if(varDeclPart != null) {
      varDeclPart.genCode(f);
    }

    statmList.genCode(f);
    f.level--;
  }
}
