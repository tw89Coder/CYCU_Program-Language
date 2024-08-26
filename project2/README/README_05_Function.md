# class Function

## Introduction

這個類別表示一個 `Function`，並儲存其相關資訊，如名稱、返回類型、內容（token 列表）和參數列表。

## Property

- **name (String)**: `Function` 的名稱。

- **type (TokenType)**: `Function` 的返回類型，由 `TokenType` 枚舉表示。

- **content (ArrayList<Token>)**: 存儲 `Function` 內容的 `Token` 列表。

- **parameterList (ArrayList<Object>)**: 儲存 `Function` 參數的列表，可以包含 `Array` 和 `Variable` 物件。

## Method

- **Function(String name, TokenType type)**: 建構子，用於創建具有指定名稱和返回類型的 `Function`。

- **Function(String name, TokenType type, ArrayList<Token> content, ArrayList<Object> parameterList)**: 建構子，用於創建具有指定名稱、返回類型、內容和參數列表的 `Function`。

- **String getName()**: `name` 的 Getter 方法。

- **void setName(String name)**: `name` 的 Setter 方法。

- **TokenType getType()**: `type` 的 Getter 方法。

- **void setType(TokenType type)**: `type` 的 Setter 方法。

- **ArrayList<Token> getContent()**: `content` 的 Getter 方法，返回存儲在函式中的 `Token` 列表。

- **void setContent(ArrayList<Token> content)**: `content` 的 Setter 方法，用於設定函式的內容。

- **void addToken(Token token)**: 方法，用於向函式的內容中添加一個 `Token`。

- **ArrayList<Object> getParameterList()**: `parameterList` 的 Getter 方法，返回函式的參數列表。

- **void setParameterList(ArrayList<Object> parameterList)**: `parameterList` 的 Setter 方法，用於設定函式的參數列表。

- **void addVariable(Variable variable)**: 方法，用於向函式的參數列表中添加一個 `Variable`。

- **void addArray(Array array)**: 方法，用於向函式的參數列表中添加一個 `Array`。

- **Variable getVariable(int index)**: 方法，根據索引從參數列表中檢索 `Variable`，若索引處的參數不是 `Variable`，則拋出 `ClassCastException`。

- **Array getArray(int index)**: 方法，根據索引從參數列表中檢索 `Array`，若索引處的參數不是 `Array`，則拋出 `ClassCastException`。

- **void printListFunction()**: 以格式化字串的形式打印函式的內容，包括適當的縮排和新行處理。
  - 變數初始化：
    - isNewLine：用於追蹤是否在新行的開始。初始值為 true。
    - indentLevel：用於追蹤當前的縮排層級。初始值為 0。
  - 遍歷 content 列表：
    - 使用 for 迴圈遍歷 content 列表中的每個 Token。
  - 處理新行的開始：
    - 如果 isNewLine 為 true，則根據當前的縮排層級打印空格。
    - 如果當前是最後一個 Token，將縮排層級重置為 0。
  - 打印當前的 Token：
    - 打印當前 Token 的名稱。
  - 根據 Token 類型決定下一步操作：
    - 如果 Token 是分號，結束當前行並設置 isNewLine 為 true。
    - 如果 Token 是左大括號，增加縮排層級並設置 isNewLine 為 true。
    - 如果 Token 是右大括號，打印換行符並設置 isNewLine 為 true。
    - 如果還有更多的 Token，檢查下一個 Token 的類型，根據情況打印空格。
  - 處理右大括號：
    - 如果下一個 Token 是右大括號，減少縮排層級。
