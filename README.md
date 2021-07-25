# WebScraper

Scrape famous JavaScript libraries used among top N results of Google Search for a Keyword


TODO:
- Writing more Test cases


- Fixing the Hashmap concurrent update method

  
- De-Duplication algorithm:
    - Step 1: For min and normal file - I would have extracted the actual file name without any identifier("min") and increment count on basis of it.
    - Step 2: For file with different names - I would have generated hashcode on basis of file's content to compare, store and increment the count.
