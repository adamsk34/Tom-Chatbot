### Shortcomings

Tom's grammar can be flawed. Example: He doesn't know of a difference between the words "is" and "are", because while one is singular and one is plural, they effectively have the same meaning.

I have refactored this project somewhat, but their is more work that should be done. When there is an error, Tom prints it to screen but he should throw an exception. Classes and methods alike are too long and should be split up. Classes and methods should have only 1 responsibility. There is a lot of duplication. Many of these problems could potentially be fixed be changing the class-structure.

Tom's NLP can be inelegant, and adding new meanings requires modification of Tom's source code. Tom only takes 1 meaning from user input even though the user input may contain more.

Testing: There are 0 tests for this project, and that is a problem. Small changes to the code of Tom's NLP can have large impacts. I propose designing a suite of test inputs that are sent to Tom automatically. They can be single session inputs (1 input per session), or several inputs sent one after another, within the same session. Tom would pass a test depending on if he (A) understood the input and (B) responded appropriately. Analytics would show a % of inputs passed.
