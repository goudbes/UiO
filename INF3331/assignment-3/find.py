#!/usr/bin/env python
# Run at least with python3.6 find.py <keyword> <directory>
import glob
import os
import sys


def main(argv):
    if len(sys.argv) != 3:
        print("Invalid arguments, exiting...")
        sys.exit(1)

    search_word = str(sys.argv[1])
    directory = str(sys.argv[2])

    if os.path.exists(directory) and os.path.isdir(directory):
        for filename in glob.iglob(directory + '/**', recursive=True):
            if search_word in filename:
                print(filename)
    else:
        print("Directory doesn't exist")
        sys.exit(1)

main(sys.argv)
