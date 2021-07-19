        .globl sprinter
        # Name: sprinter
        # Synopsis: Simplified version of sprintf (løsning)
        # C-signature: int spinter(unsigned char* res, unsigned char* format, ...)

sprinter:
        PUSHL %EBP              # Store the current stack frame
        MOVL %ESP,%EBP          # Preserve esp to ebp for argument references

        # Use i and j counters as the two first variables
        # on the stack. Need to use pushl to update esp

        PUSHL $0                # i as -4(%EBP)
        PUSHL $0                # j as -8(%EBP)
        LEAL 12(%EBP), %EAX     # Vararg pointer
        PUSHL %EAX              # Vararg pointer as variable (-12(%EBP))
        PUSHL $0                # k as -16(%EBP)
        JMP .LOOP_BODY

        .LOOP_START:
        ADDL $1, -4(%EBP)       # i++

        .LOOP_BODY:
        MOVL 12(%EBP), %EAX     # Second arg format
        ADDL -4(%EBP), %EAX     # Move to i in format
        MOVZBL (%EAX), %EBX     # Take value of format[i]

        CMP $37, %EBX           # Check for '%'
        JNE .COPY_CHAR

        ADDL $1, -4(%EBP)       # i++
        ADDL $1, %EAX           # Move to next char after '%'
        MOVZBL (%EAX), %EBX     # Take value of format[i+1]
        CMP $99, %EBX           # Check for 'c'
        JE .COPY_CHAR_ARG
        CMP $37, %EBX           # Check for '%'
        JE .PERCENT
        CMP $115, %EBX          # Check for 's'
        JE .STRING
        CMP $100, %EBX          # Check for 'd'
        JE .INT
        CMP $117, %EBX          # Check for 'u'
        JE .UNSIGNED_INT
        CMP $35, %EBX           # Check for '#'
        JNE .CASE_X
        ADDL $1, -4(%EBP)       # i++
        ADDL $1, %EAX           # Move to next char after '#'
        MOVZBL (%EAX), %EBX     # Take value of format[i+1]
        CMP $120, %EBX          # Check for 'x'
        JNE .INVALID
        JMP .HEX_PREFIX
        .CASE_X:
        CMP $120, %EBX          # Check for 'x'
        JE .HEX
        .INVALID:
        MOVL $-1, %EAX          # No case matched, return -1
        JMP .DONE

        .HEX_PREFIX:
        MOVL 8(%EBP), %EBX      # First arg res
        ADDL -8(%EBP), %EBX     # res[j]
        MOVL $48, (%EBX)        # res[j] = '0'
        ADDL $1, %EBX           # res[j+1]
        MOVL $120, (%EBX)       # res[j] = 'x'
        ADDL $2, -8(%EBP)       # j+=2

        .HEX:
        ADDL $4, -12(%EBP)      # Next vararg
        MOVL -12(%EBP), %EBX    # Vararg pointer
        MOVL (%EBX), %EAX       # Vararg value
        PUSHL $0xFF             # End of stack marker (to track digits)

        MOVL $16, %ECX          # divisor
        .DIV_HEX_LOOP:
        MOVL $0, %EDX           # Null EDX before division
        IDIVL %ECX              # val/16 -> EAX,EDX
        PUSHL %EDX              # Store last digit
        CMP $0, %EAX            # Check if we are done
        JNE .DIV_HEX_LOOP

        .HEX_POP_LOOP:
        POPL %EAX               # Read back digit
        CMPL $0xFF, %EAX        # Check if stack marker
        JE .LOOP_START
        CMP $9, %EAX            # Check for number or letter
        JG .HEX_LETTERS
        ADDL $48, %EAX          # Convert to ascii
        MOVL 8(%EBP), %EBX      # First arg res
        ADDL -8(%EBP), %EBX     # res[j]
        MOVL %EAX, (%EBX)       # res[j] = digit
        ADDL $1, -8(%EBP)       # j++
        JMP .HEX_POP_LOOP
        .HEX_LETTERS:
        ADDL $87, %EAX          # Ascii value (97'a' - 10)
        MOVL 8(%EBP),%EBX       # First arg res
        ADDL -8(%EBP), %EBX     # res[j]
        MOVL %EAX, (%EBX)       # res[j] = digit
        ADDL $1, -8(%EBP)       # j++
        JMP .HEX_POP_LOOP

        .UNSIGNED_INT:
        ADDL $4, -12(%EBP)      # Next vararg
        MOVL -12(%EBP), %EBX    # Vararg pointer
        MOVL (%EBX), %EAX       # Vararg value
        PUSHL $0xFF             # End of stack marker
        JMP .INT_POS            # Skip negative cmp

        .INT:
        ADDL $4, -12(%EBP)      # Next vararg
        MOVL -12(%EBP), %EBX    # Vararg pointer
        MOVL (%EBX), %EAX       # Vararg value
        PUSHL $0xFF             # End of stack marker

        CMPL $0, %EAX           # Positive or negative
        JNS .INT_POS

        # Using separate loop for negative numbers
        # could optimally be one loop with some
        # further work.

        NEGL %EAX               # Negate
        MOVL $10, %ECX          # divisor
        .DIV_LOOP_NEG:
        MOVL $0, %EDX           # Null EDX before division
        IDIVL %ECX              # val/10 -> EAX,EDX
        PUSHL %EDX              # Store last digit
        CMP $0, %EAX            # Check if we are done
        JNE .DIV_LOOP_NEG
        PUSHL $-3               # Add '-' sign - 48
        JMP .POP_LOOP

        .INT_POS:
        MOVL $10, %ECX          # divisor
        .DIV_LOOP:
        MOVL $0, %EDX           # Null EDX before division
        IDIVL %ECX              # val/10 -> EAX,EDX
        PUSHL %EDX              # Store last digit
        CMP $0, %EAX            # Check if we are done
        JNE .DIV_LOOP

        .POP_LOOP:
        POPL %EAX               # Read back digit
        CMPL $0xFF, %EAX        # Check if stack marker
        JE .LOOP_START
        ADDL $48, %EAX          # Convert to ascii
        MOVL 8(%EBP), %EBX      # First arg res
        ADDL -8(%EBP), %EBX     # res[j]
        MOVL %EAX, (%EBX)       # res[j] = digit
        ADDL $1, -8(%EBP)       # j++
        JMP .POP_LOOP

        .STRING:
        ADDL $4, -12(%EBP)      # Next vararg
        MOVL $0, -16(%EBP)      # k = 0
        .STRING_LOOP:
        MOVL -16(%EBP), %EDX    # k in edx
        MOVL -12(%EBP), %EAX    # Vararg pointer
        MOVL (%EAX), %EAX       # Follow pointer
        ADDL %EDX, %EAX         # vararg[k]
        MOVL 8(%EBP), %ECX      # res pointer
        ADDL -8(%EBP), %ECX     # res[j]
        MOVZBL (%EAX), %EAX     # Last byte of vararg
        CMP $0, %AL             # stop at \0
        JE .LOOP_START
        MOVB %AL, (%ECX)        # res[j] = vararg[k]
        ADDL $1, -8(%EBP)       # j++
        ADDL $1, -16(%EBP)      # k++
        JMP .STRING_LOOP

        .PERCENT:
        MOVL 8(%EBP), %EAX      # First arg res
        ADDL -8(%EBP), %EAX     # res[j]
        MOVL $37, (%EAX)        # res[j] = %
        ADDL $1, -8(%EBP)       # j++
        JMP .LOOP_START

        .COPY_CHAR_ARG:
        ADDL $4, -12(%EBP)      # Next vararg
        MOVL -12(%EBP), %EAX    # Vararg pointer
        MOVZBL (%EAX), %EBX     # Vararg value
        MOVL 8(%EBP), %EAX      # First arg res
        ADDL -8(%EBP), %EAX     # res[j]
        MOVL %EBX, (%EAX)       # res[j] = vararg[0]
        ADDL $1, -8(%EBP)       # j++
        JMP .LOOP_START

        .COPY_CHAR:
        MOVL 8(%EBP), %EAX      # First arg res
        ADDL -8(%EBP), %EAX     # res[j]
        MOVL 12(%EBP), %ECX     # Second arg format
        ADDL -4(%EBP), %ECX     # format[i]
        MOVZBL (%ECX), %EDX     # *format[i]
        MOVL %EDX, (%EAX)       # res[j] = format[i]
        ADDL $1, -8(%EBP)       # j++

        CMP $0, %DL             # Compare lower byte to \0
        JNE .LOOP_START

        MOVL -8(%EBP), %EAX     # Return j
        CMPL $0, %EAX           # If format is empty return 0
        JE .DONE
        SUBL $1, %EAX           # If not return j-1 (minus \0)

        .DONE:
        MOVL %EBP,%ESP          # Leave
        POPL %EBP               # Leave
        RET                     # Return
