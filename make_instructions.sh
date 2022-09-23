#!/bin/bash
pandoc --shift-heading-level-by=-1 -s README.md -o instructions.html -f gfm --css pandoc.css
xdg-open instructions.html
