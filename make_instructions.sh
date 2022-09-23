#!/bin/bash
pandoc -s README.md -o instructions.html -f gfm --css pandoc.css
xdg-open instructions.html
