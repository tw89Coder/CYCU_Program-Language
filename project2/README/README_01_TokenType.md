# class TokenType

## Introduction

將 OurC grammar 中的 `the lexical part` 使用枚舉方便後續使用。
在枚舉中，每一個都可以當作 terminal symbol 使用。

## Token Types

與 `the lexical part` 中的 Constant 不同，我將原本的 Constant 細分為  
> CONSTANT_INT,      // 23, 023 
>
> CONSTANT_FLOAT,    // .0200, 0.200, 3.200 
>
> CONSTANT_CHAR,     // 'c'
> 
> CONSTANT_STRING,   // "This is a string"
> 
> CONSTANT_FALSE,    // false
> 
> CONSTANT_TRUE      // true

這是為了能夠在後續的 project 能夠更加方便進行計算。

> [!NOTE] 
> 當我在進行 project3 時，我認為應該要將 `CONSTANT_FALSE` 與 `CONSTANT_TRUE` 合併為 `CONSTANT_BOOLEAN` 比較適合。

## Error Handling

另外 `ERROR` 是用來給發生 `lexical error` 的 token 所賦予的 tokenType，在 class Parser 中的 match() 將會因為 `ERROR` 而知道這是一個錯誤的 token，從而 return false。

## Temporary and Additional Types

`OBJECT` 是給有需要使用額外的功能或是暫時頂替所預備的 type，主要的目的是應急。

> [!WARNING] 
> 我在 project 3 中忘記我有創建這一種 type，導致後續又建立了 `NOP`，其實兩個用途相同。