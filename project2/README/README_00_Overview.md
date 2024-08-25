# PL112_10927262

ourC Interpreter 是一個用於學習和教學目的的簡單直譯器。這個專案展示了如何從零開始設計和實現一個基本的直譯器。

## Project2 目標

- 詞法分析：能夠對使用者輸入進行 token 分類，以及輸出 lexical error。
- 語義以及語法分析：判斷使用者的文法是否正確，輸出 syntactical error 與 semantic error。
- 擴展：提供一個基礎，可以擴展為更複雜的直譯器。

> [!CAUTION]
> 這一個程式在 Project2 完成後不會產生語法樹(AST)，我所設計的 parser 會在 project3 才會生成 AST。

## 系統架構

Project2 的架構分為以下幾個部分：

1. **TokenType**     : toekn 的各種 type 的枚舉(grammar 中的 "the lexical part")。
2. **Token**         : 儲存 token 的基本資料，用來建立 token。
3. **Variable**      : 儲存 variable 的基本資料，用來建立 variable。
4. **Array**         : 儲存 array 的基本資料，用來建立 array。
5. **Function**      : 儲存 function 的基本資料，用來建立 function。
6. **ErrorMessage**  : 輸出錯誤訊息的格式。可以確認 undefined variable，以及為以後的 project 做準備。
7. **Scope**         : 用來處理變數可視範圍
8. **Tokenizer**     : 取得使用者的輸入，以及每次回傳一個 token。
9. **Parser**        : 判斷語義以及語法是否合法。
10. **Main**         : 主程式入口。

