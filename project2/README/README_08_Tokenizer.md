# class Tokenizer

`Tokenizer` 類別是一個詞法分析器，用於從用戶輸入中提取並識別各種標記（tokens）。它可以處理不同的語法符號、關鍵字、數字、字串等，並生成對應的標記列表。以下是該類別的詳細說明。

## 功能

- **Initialization**：
  - 建立 `Tokenizer` 實例，設置基本屬性並準備接收輸入。

- **Resetting**：
  - `reset(boolean isPause)`：重置 `userInput` 和 `currentIndex`，根據 `isPause` 決定是否清除輸入內容。
  - `resetLine()`：重置當前行數，並在必要時清除 `userInput`。
  - `resetTokenList()`：清空標記列表。

- **Getting the Next Token**：
  - `getNextToken()`：讀取並解析下一個標記。處理不同的語法符號和標記類型，如運算符、數字、字串等。

## Token Handling

> [!TIP]
> 使用有限狀態機去建立 Tokenizer，每一個 token 的開頭字元將會決定進入哪一種狀態。
>
> 如果是兩個以上字元的 token，就向後看看是否符合當前的內容去判斷，當然要注意不能 out of bound。

`getNextToken()` 方法會根據當前字符處理以下標記類型：

- **Operators**：
  - `+`, `-`, `*`, `/`, `%`, `^`, `>`, `<`, `&`, `|`, `!`
  - 處理增量、減量、賦值、算術和位運算等操作符。

> [!TIP]
> 對於註解，index 不會直接移到最後面，因為最後一定是自己加入的 "\n"，而 "\n" 需要去增加 line 的數量。

- **Delimiters**：
  - `;`, `,`, `?`, `:`, `(`, `)`, `[`, `]`, `{`, `}`

- **String and Character Literals**：
  - `"`：處理字串字面量。
  - `'`：處理字符字面量。

- **Numbers**：
  - 整數和浮點數。

- **Identifiers and Keywords**：
  - 識別符（變數名等）和語言關鍵字（如 `if`, `else`, `while` 等）。

> [!TIP]
> StringBuilder 比 String 更好，主要因為它更高效。當你需要多次修改或拼接字串時，StringBuilder 可以直接在內部的字符序列上進行操作，不需要每次都創建新的字串對象，這樣可以節省內存和提高性能。簡單來說，StringBuilder 適合需要頻繁修改字串的場景，而 String 則適合字串不變的情況。

- **Error Handling**：
  - 對無效字符和語法錯誤進行報錯。

## Properties

- `scanner`：用於讀取用戶輸入的 `Scanner` 實例。
- `userInput`：用戶輸入的字符串。
- `currentIndex`：當前解析的索引。
- `line`：當前行數。
- `tokenList`：存儲標記的列表，用來輸出字定義 function 的內容。
- `errorMessage`：處理錯誤信息的對象。
- `isTest`：控制是否進行測試輸出。

## Methods

### `reset(boolean isPause)`

重置詞法分析器的狀態。根據 `isPause` 參數決定是否清除 `userInput` 和 `currentIndex`。

### `resetLine()`

重置行數，並根據需要清除 `userInput`。

### `resetTokenList()`

清空標記列表。

### `getNextToken()`

解析並返回下一個標記。

### `handlePlus()`, `handleMinus()`, `handleMultiply()`, `handleAssignment()`, `handleModulus()`, `handleGreaterThan()`, `handleLessThan()`, `handleBitwiseAnd()`, `handleBitwiseOr()`, `handleNot()`

處理不同類型的操作符。

### `handleStringLiteral()`

處理字串字面量。

### `handleCharacterLiteral()`

處理字符字面量。

### `handleNumber()`

處理數字（整數或浮點數）。

### `handleIdentifierOrKeyword()`

處理識別符或關鍵字。

## Getter and Setter Methods

- `getTokenList()`：獲取當前的標記列表。
- `setTokenList(ArrayList<Token> tokenList)`：設置標記列表。
- `getUserInput()`：獲取當前用戶輸入。
- `getCurrentIndex()`：獲取當前索引。
- `getLine()`：獲取當前行數。
