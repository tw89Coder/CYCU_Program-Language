# class Scope

## Introduction

`Scope` 類別負責管理變數、陣列和函式的作用域。它支援作用域嵌套，以便在不同的作用域層次中存取和更新變數、陣列和函式。`Scope` 類別還支援查詢和列出目前作用域中定義的元素。

## Property

- `variables (Map<String, Variable>)`: 儲存當前作用域中的變數。
- `arrays (Map<String, Array>)`: 儲存當前作用域中的陣列。
- `functions (Map<String, Function>)`: 儲存當前作用域中的函式。
- `predefineVariable (Map<String, Variable>)`: 預定義變數（如 `cin` 和 `cout`）。
- `predefineFunctions (Map<String, Function>)`: 預定義函式（如 `ListAllVariables`）。
- `parentScope (Scope)`: 父作用域，若當前作用域是頂層則為 `null`。

## Constructor method

- **Scope(Scope parentScope)**: 初始化一個新的作用域，並可指定父作用域。

  - `parentScope (Scope)`: 新作用域的父作用域。

## Method

### Variable operate

- **void addOrUpdateVariable(Variable variable)**: 
  將變數新增到當前作用域或更新現有變數。

  - `variable (Variable)`: 要新增或更新的變數。

- **Variable getVariable(String name)**: 
  根據名稱獲取變數，如果當前作用域沒有找到，則在父作用域中查找。

  - `name (String)`: 變數名稱。
  - **返回值**: 找到的變數，若未找到則返回 `null`。

- **boolean containsVariableName(String name)**: 
  檢查當前作用域或父作用域是否包含指定名稱的變數。

  - `name (String)`: 變數名稱。
  - **返回值**: 若找到則返回 `true`，否則返回 `false`。

### Array operate

- **void addOrUpdateArray(Array array)**: 
  將陣列新增到當前作用域或更新現有陣列。

  - `array (Array)`: 要新增或更新的陣列。

- **Array getArray(String name)**: 
  根據名稱獲取陣列，如果當前作用域沒有找到，則在父作用域中查找。

  - `name (String)`: 陣列名稱。
  - **返回值**: 找到的陣列，若未找到則返回 `null`。

- **boolean containsArrayName(String name)**: 
  檢查當前作用域或父作用域是否包含指定名稱的陣列。

  - `name (String)`: 陣列名稱。
  - **返回值**: 若找到則返回 `true`，否則返回 `false`。

### Function operate

- **void addOrUpdateFunction(Function function)**: 
  將函式新增到當前作用域或更新現有函式。

  - `function (Function)`: 要新增或更新的函式。

- **Function getFunction(String name)**: 
  根據名稱獲取函式，如果當前作用域沒有找到，則在父作用域中查找。

  - `name (String)`: 函式名稱。
  - **返回值**: 找到的函式，若未找到則返回 `null`。

- **boolean containsFunctionName(String name)**: 
  檢查當前作用域或父作用域是否包含指定名稱的函式。

  - `name (String)`: 函式名稱。
  - **返回值**: 若找到則返回 `true`，否則返回 `false`。

### Predefine element

- **boolean containsPredefineFunctionsName(String name)**: 
  檢查當前作用域是否包含指定名稱的預定義函式。

  - `name (String)`: 預定義函式名稱。
  - **返回值**: 若找到則返回 `true`，否則返回 `false`。

- **boolean containsPredefineVariableName(String name)**: 
  檢查當前作用域是否包含指定名稱的預定義變數。

  - `name (String)`: 預定義變數名稱。
  - **返回值**: 若找到則返回 `true`，否則返回 `false`。

### Scope operate

- **Scope enterScope()**: 
  進入新的子作用域並返回該作用域。

  - **返回值**: 新的子作用域。

- **Scope exitScope()**: 
  離開當前作用域並返回父作用域。

  - **返回值**: 父作用域。

> [!WARNING]  
> 程式碼中的i `exitScope()` 沒有用處，本來是要刪除或是註解掉的，但是我忘記了。
> 當初用這一個 method 是因為想說有 enter 就要有 exit，所以就先寫了，結果用不到。

### 列表功能

- **void listVariables()**: 
  列出當前作用域中所有變數，按名稱字母順序排列。

- **void listArrays()**: 
  列出當前作用域中所有陣列，按名稱字母順序排列。

- **void listFunctions()**: 
  列出當前作用域中所有函式，按名稱字母順序排列。

- **void listNamesSortedByName()**: 
  列出當前作用域中所有變數和陣列名稱，按名稱字母順序排列。

- **void listFunctionsSortedByName()**: 
  列出當前作用域中所有函式名稱，按名稱字母順序排列。

### Order method

- **private void bubbleSort(List<String> list)**: 
  使用冒泡排序法對字串列表進行排序。

  - `list (List<String>)`: 需要排序的字串列表。
