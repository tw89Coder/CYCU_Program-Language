# class Main

## Introduction

`Main` 類別是應用程式的入口點，負責執行程式的主要邏輯。在此類別中，根據不同的調試模式（TokenizerDEBUG 或 ParserDEBUG），會啟動不同的功能。

## Property

- `static final boolean TokenizerDEBUG`: 指定是否啟用 Tokenizer 調試模式。預設為 `false`。
- `static final boolean ParserDEBUG`: 指定是否啟用 Parser 調試模式。預設為 `true`。

## Method

### main(String[] args)

程序的主入口點，根據設定的調試模式來選擇執行 Tokenizer 或 Parser。

- **調試模式為 TokenizerDEBUG**

  - 如果 `TokenizerDEBUG` 為 `true`，則會啟動 `Tokenizer` 進行詞法分析。程式會持續讀取並顯示標記（Token），直到遇到名稱為 `"Done"` 的標記為止，這時候程式會顯示 `"Our-C exited ..."` 並結束運行。

  ```java
  Tokenizer tokenizer = new Tokenizer();
  System.out.println("Our-C running ...");

  while (true) {
      Token token = tokenizer.getNextToken();
      System.out.println(token);

      if (token.getName().equals("Done")) {
          System.out.println("> Our-C exited ...");
          break;
      }
  }
