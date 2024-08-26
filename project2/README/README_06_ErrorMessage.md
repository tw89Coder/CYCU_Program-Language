# class ErrorMessage

## Introduction

`ErrorMessage` 類別負責處理和輸出不同類型的錯誤訊息，包括詞法錯誤、語法錯誤和語意錯誤。每種錯誤方法會輸出相關的錯誤訊息以協助偵錯。

## Method

- **void lexicalError(char firstChar, int line)**: 
  用於處理詞法錯誤。當偵測到無法識別的 token 時，輸出一條包含行數和起始字符的錯誤訊息。

  - `firstChar (char)`: 引起詞法錯誤的起始字符。
  - `line (int)`: 該字符所在的行數。

- **void syntacticalError(Token token)**: 
  用於處理語法錯誤。當偵測到不預期的 token 時，輸出一條包含行數和該 token 名稱的錯誤訊息。

  - `token (Token)`: 引起語法錯誤的 token，其包含行數和名稱資訊。

- **void semanticError(Token token)**: 
  用於處理語意錯誤。當偵測到未定義的標識符時，輸出一條包含行數和該標識符名稱的錯誤訊息。

  - `token (Token)`: 引起語意錯誤的 token，其包含行數和名稱資訊。
