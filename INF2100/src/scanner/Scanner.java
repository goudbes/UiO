package scanner;

import main.Main;
import static scanner.TokenKind.*;

import java.io.*;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public class Scanner {

  //Current token and next token
  public Token curToken = null, nextToken = null;

  private LineNumberReader sourceFile = null;
  private String sourceFileName, sourceLine = "";
  private int sourcePos = 0;

  public Scanner(String fileName) {
    sourceFileName = fileName;
    try {
      sourceFile = new LineNumberReader(new FileReader(fileName));

    } catch (FileNotFoundException e) {
      Main.error("Cannot read " + fileName + "!");
    }

  }


  public String identify() {
    return "Scanner reading " + sourceFileName;
  }


  public int curLineNum() {
    if(curToken != null)
    return curToken.lineNum;
    else
    return getFileLineNum();
  }

  private void error(String message) {
    Main.error("Scanner error on " +
    (curLineNum()<0 ? "last line" : "line "+curLineNum()) +
    ": " + message);
  }

  public void p(String s) {
    System.out.println(s);
  }

  public void readNextToken() {
    curToken = nextToken;  nextToken = null;
    int isComment = 0;
    boolean isCurlyComment = false;
    boolean isWord = false;
    boolean isNumber = false;
    int startWord = -1;
    while (true) {

      if(sourcePos >= sourceLine.length()) {
        readNextLine();
      }
      if(sourceFile != null) {

        char c = sourceLine.charAt(sourcePos);
        // p("" + c + " isWord = " + isWord + " isNumber = " + isNumber);

        if (sourcePos < sourceLine.length() && c=='/' && sourceLine.charAt(sourcePos+1)=='*') {
          if(isComment>0){
            if(!isCurlyComment) {
              Main.error(curLineNum(), "Start of /* comment inside comment");
            }
          } else {
            isCurlyComment = false;
          }
          isComment++;
          sourcePos+=2;
          continue;
        }

        if (sourcePos < sourceLine.length() && c=='{') {
          if(isComment>0) {
            if(isCurlyComment) {
              Main.error(curLineNum(), "Start of { comment inside comment");
            }
          } else {
            isCurlyComment = true;
          }
          isComment++;
          sourcePos++;
          continue;
        }

        if (isComment>0) {
          if (sourcePos < sourceLine.length() && c=='*' && sourceLine.charAt(sourcePos+1)=='/') {
            isComment--;
            sourcePos+=2;
            continue;
          } else if (sourcePos < sourceLine.length() && c=='}') {
            isComment--;
            sourcePos++;
            continue;
          } else {
            sourcePos++;
            continue;
          }
        }

        if(c == '\'') {
          // Initially assumed multi-char strings
          // so more complicated than needed :(
          int start = sourcePos;
          boolean ended=false;
          while(sourcePos < sourceLine.length() - 1 ) {
            if( sourceLine.charAt(sourcePos+1) != '\'') {
              sourcePos++;
            } else if (sourcePos < sourceLine.length()-4) {
              if(sourceLine.substring(sourcePos+1, sourcePos+5).equals("''''")) {
                sourcePos += 4;
              } else {
                ended = true;
                break;
              }
            } else {
              ended = true;
              break;
            }
          }
          if(!ended) {
            testError("'");
          }
          String word = sourceLine.substring(start, sourcePos+2);
          word = word.replace("''''", "'");
          newToken(word.charAt(1), false);
          sourcePos+=2;
          return;
        }


        if(sourcePos < sourceLine.length() - 1) {
          String dual = sourceLine.substring(sourcePos, sourcePos+2);
          if(dual.equals(":=") || dual.equals(">=") ||
          dual.equals("<=") || dual.equals("<>") ||
          dual.equals("..")) {
            if(isNumber || isWord) {
              if(isNumber) {
                int n = Integer.parseInt(sourceLine.substring(startWord, sourcePos));
                newToken(n);
              } else {
                String word = sourceLine.substring(startWord, sourcePos);
                newToken(word);
              }
              isNumber = false;
              isWord = false;
              return;
            }
            TokenKind kind = TokenKind.fromString(dual);
            newToken(kind);
            sourcePos+=2;
            return;
          }
        }

        if(isDigit(c)) {
          if(!isWord) {
            if (isNumber) {
              sourcePos++;
              continue;
            } else {
              isNumber = true;
              startWord = sourcePos;
              sourcePos++;
              continue;
            }
          } else {
            sourcePos++;
            continue;
          }
        } else if (isNumber) {
          isNumber = false;
          String word = sourceLine.substring(startWord, sourcePos);
          int n = Integer.parseInt(word);
          newToken(n);
          return;
        }


        if (isLetterAZ(c)) {
          if(isWord==false) { //[2..Limit]
            isWord = true;
            startWord = sourcePos;
          }
        } else {
          if(isWord){
            String word = sourceLine.substring(startWord, sourcePos);
            newToken(word);
            isWord = false;
            return;
          } else if(isNumber) {
            int n = Integer.parseInt(sourceLine.substring(startWord, sourcePos));
            newToken(n);
            isNumber = false;
            return;
          }
        }

        if (isSingleToken(c)) {
          try {
            String word = sourceLine.substring(sourcePos, sourcePos+1);
            TokenKind kind = TokenKind.fromString(word);
            newToken(kind);
          } catch(IllegalArgumentException e) {
            newToken(c);
          }
          sourcePos++;
          return;
        } else if(!isWord && (c != ' ' && c != '\t')) {
          Main.error(curLineNum(),"Illegal character found: " + Character.toString(c));
        }

        sourcePos++;
      } else {
        nextToken = new Token(TokenKind.eofToken, getFileLineNum());

        if(isComment>0 && nextToken.kind == TokenKind.eofToken) {
          readNextToken();
          testError("comment ending");
        }

        Main.log.noteToken(nextToken);
        return;
      }
    }
  }

  private void newToken(TokenKind kind) {
    nextToken = new Token(kind, getFileLineNum());
    Main.log.noteToken(nextToken);
  }

  private void newToken(char c) {
    newToken(c, true);
  }

  private void newToken(char c, boolean lower) {
    if(lower) {
      nextToken = new Token(Character.toLowerCase(c), getFileLineNum());
    } else {
      nextToken = new Token(c, getFileLineNum());
    }
    Main.log.noteToken(nextToken);
  }

  private void newToken(int n) {
    nextToken = new Token(n, getFileLineNum());
    Main.log.noteToken(nextToken);
  }

  private void newToken(String word) {
    nextToken = new Token(word.toLowerCase(), getFileLineNum());
    Main.log.noteToken(nextToken);
  }

  private void readNextLine() {
    if (sourceFile != null) {
      try {
        sourceLine = sourceFile.readLine();
        if (sourceLine == null) {
          sourceFile.close();
          sourceFile = null;
          sourceLine = "";
        } else {
          sourceLine += " ";
        }
        sourcePos = 0;
      } catch (IOException e) {
        Main.error("Scanner error: unspecified I/O error!");
      }
    }
    if (sourceFile != null)
    Main.log.noteSourceLine(getFileLineNum(), sourceLine);
  }


  private int getFileLineNum() {
    return (sourceFile!=null ? sourceFile.getLineNumber() : 0);
  }


  // Character test utilities:

  private boolean isLetterAZ(char c) {
    return 'A'<=c && c<='Z' || 'a'<=c && c<='z';
  }


  private boolean isDigit(char c) {
    return '0'<=c && c<='9';
  }

  private boolean isWordChar(char c) {
    return isLetterAZ(c) || isDigit(c);
  }

  private boolean isSingleToken(char c) {
    switch(c) {
      case '+':
      case ':':
      case ',':
      case '.':
      case '=':
      case '>':
      case '[':
      case '(':
      case '<':
      case '*':
      case ']':
      case ')':
      case ';':
      case '-':
      return true;
    }
    return false;
  }



  // Parser tests:

  public void test(TokenKind t) {
    if (curToken.kind != t)
    testError(t.toString());
  }

  public void testError(String message) {
    Main.error(curLineNum(),
    "Expected a " + message +
    ((curToken != null) ? " but found a " + curToken.kind : " but found nothing ") + "!");
  }

  public void skip(TokenKind t) {
    test(t);
    readNextToken();
  }
}
