# Building-an-AST-Eclipse-Plugin

## Introduction

This document describes how to build a relatively useless eclipse plug-in. The plug-in adds an option to the drop down menu that appears when right clicking on a user defined method (i.e., not a library method) in the Package Explorer frame. When the option is selected a dialog box will appear that states the total number of user defined methods in the project along with the number of method calls (both to library and to user defined methods) made by the defining class of the method the option was invoked on. The goal is to show an interface with some of the entities that Eclipse makes available, such as `IMethod`s, `IClassFile`s, `ICompilationUnit`s, etc. It also demonstrates how to build an Abstract Syntax Tree (AST) and how to utilize the visitor pattern to traverse its nodes.

**Note**: This document is meant to be used in conjunction with `InfoGatherer.java` and `MyVisitor.java`. You should read and understand both of these files.
