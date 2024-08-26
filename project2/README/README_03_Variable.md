# class Variable

## Introduction

這個類別表示一個 `Variable`，並儲存其相關資訊，如名稱、類型、值和運算符。

## Property

- **name (String)**: `Variable` 的名稱。

- **type (TokenType)**: `Variable` 的類型，由 `TokenType` 枚舉表示。

- **value (String)**: `Variable` 的值，以字串形式表示。

- **operator (String)**: 與 `Variable` 關聯的運算符字串，可以是指標 (`*`) 或位址 (`&`) 運算符，也可以為空。

> [!NOTE]  
> 然而 **operator (String)** 用不到，這邊應該要在 project4 使用一個 class 建立一個 node 去處理比較好。

## Method

- **Variable(String name, TokenType type, String value, String operator)**: 建構子，用於創建具有指定名稱、類型、值和運算符的 `Variable`。

- **Variable(String name, TokenType type, String operator)**: 建構子，用於創建具有指定名稱、類型和運算符的 `Variable`。值將根據類型設定為預設值。

- **String getName()**: `name` 的 Getter 方法。

- **void setName(String name)**: `name` 的 Setter 方法。

- **TokenType getType()**: `type` 的 Getter 方法。

- **void setType(TokenType type)**: `type` 的 Setter 方法。

- **String getValue()**: `value` 的 Getter 方法。

- **void setValue(String value)**: `value` 的 Setter 方法。

- **String getOperator()**: `operator` 的 Getter 方法。

- **void setOperator(String operator)**: `operator` 的 Setter 方法。

- **void printListVariable()**: 以格式化字串的形式打印變數的類型和名稱。

- **String toString()**: 用來輸出所有 `Variable` 資訊的字串，便於檢查。
