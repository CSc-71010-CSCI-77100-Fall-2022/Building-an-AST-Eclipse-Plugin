#!/bin/bash
pandoc README.md -o instructions.html -f markdown_github --css pandoc.css
xdg-open instructions.html
