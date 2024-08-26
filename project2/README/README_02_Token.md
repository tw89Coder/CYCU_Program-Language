# class Token

## Introduction

這裡表示 `Token` 所儲存的訊息。

## Property

- **name (String)**: `Token` 的名字。

- **type (TokenType)**: 所屬於的 `TokenType`。

- **line (int)**: `Token` 所在的行數，用於 Error Message。

## Method

- **Token()**: 建構子。

- **Token(String name, TokenType type, int line)**: 建構子。

- **String getName()**: Getter of name.

- **void setName(String name)**: Setter of name.

- **TokenType getType()**: Getter of TokenType.

- **void setType(TokenType type)**: Setter of TokenType.

- **int getLine()**: Getter of line.

- **void setLine(int line)**: Setter of line.

- **String toString()**: 檢查用，用來輸出轉為字串的所有資訊。
