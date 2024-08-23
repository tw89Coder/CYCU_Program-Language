# CYCU_Program-Language

This program was developed by 10927262, Yi-Hung Lu. Its main function is to implement an interpreter following ourC grammar. I hope you find it useful!

- Project2 can pass all test case.

- Project3 will be an abnormal stop at the last question(Hidden test data).

- Project4 no time to do it.
  - But is also build tree node and get data.
  - Can add list of class Define in class UserInput. (UserInput just store class Define, and not associate it)
  - Maybe class scope should do some change. (For &a, &g)
    - formal_parameter_list link actual_parameter_list, and synchronously update parameters.
  - Make class FunctionCall extend ExpressionNode. It will associate the same name class Define in the class UserInput's define list.
