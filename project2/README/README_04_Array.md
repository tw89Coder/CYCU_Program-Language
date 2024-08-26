# class Array

## Introduction

這個類別表示一個 `Array`，並儲存其相關資訊，如名稱、類型、運算符、範圍和元素。

## Property

- **name (String)**: `Array` 的名稱。

- **type (TokenType)**: `Array` 的類型，由 `TokenType` 枚舉表示。

- **operator (String)**: 與 `Array` 關聯的運算符字串，可以是指標 (`*`) 或位址 (`&`) 運算符，也可以為空。

- **range (String)**: `Array` 的範圍，以字串形式表示。通常為一個整數，表示陣列的大小。

- **elements (Map<String, String>)**: 儲存 `Array` 元素的映射，其中鍵為索引，值為元素的值。

> [!NOTE]  
> 然而 **operator (String)** 用不到，這邊應該要在 project4 使用一個 class 建立一個 node 去處理比較好。

## Method

- **Array(String name, TokenType type, String operator, String range)**: 建構子，用於創建具有指定名稱、類型、運算符和範圍的 `Array`。根據類型和範圍初始化元素。

- **String getName()**: `name` 的 Getter 方法。

- **void setName(String name)**: `name` 的 Setter 方法。

- **TokenType getType()**: `type` 的 Getter 方法。

- **void setType(TokenType type)**: `type` 的 Setter 方法。

- **String getOperator()**: `operator` 的 Getter 方法。

- **void setOperator(String operator)**: `operator` 的 Setter 方法。

- **String getRange()**: `range` 的 Getter 方法。

- **void setRange(String range)**: `range` 的 Setter 方法。

- **Map<String, String> getElements()**: `elements` 的 Getter 方法，返回存儲在陣列中的元素。

- **void setElements(Map<String, String> elements)**: `elements` 的 Setter 方法，用於設定陣列中的元素。

- **void addValue(String value, String index)**: 方法，用於在指定索引處添加一個值。若索引超出範圍，則拋出 `IndexOutOfBoundsException`。

- **void printListVariable()**: 以格式化字串的形式打印陣列的類型和名稱，包括其範圍。

- **String toString()**: 用來輸出所有 `Array` 資訊的字串，便於檢查。

