# CYCU_Program-Language

## System Information

- **Operating System**
  - Windows 11 Home Edition
  - 23H2

- **CPU**
  - 12th Gen Intel(R) Core(TM) i7-12700H 2.30 GHz

- **RAM**
  - 32.0 GB (31.6 GB usable)

- **System Type**
  - 64-bit Operating System, x64-based processor

- **Language**
  - Java version "21.0.1"

## Project Overview

This program was developed by **10927262, Yi-Hung Lu**. Its main function is to implement an interpreter following ourC grammar. I hope you find it useful!

### Project Status

- **Project 2**
  - Passes all test cases.

- **Project 3**
  - Encounters an abnormal stop at the last question (hidden test data).

- **Project 4**
  - Not completed due to time constraints.
  - However, it involves building tree nodes and retrieving data.
  - Suggestions for improvement:
    - Add a list of class `Define` in class `UserInput` (UserInput just stores class `Define` and does not associate it).
    - Consider changes to class scope (for `&a`, `&g`).
      - `formal_parameter_list` should link to `actual_parameter_list` and update parameters synchronously.
    - Make class `FunctionCall` extend `ExpressionNode`. It will associate with the same name class `Define` in the `UserInput`'s define list.
