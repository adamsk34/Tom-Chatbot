# Tom-Chatbot

This is a personal project, a rule-based chatbot. Made mostly in 2017. Unlike many chatbots, Tom is not made to fulfill any purpose other than to have a simple chat.

## Getting Started

The file [src/App.java](src/App.java) contains the `main()` method, run this to have a chat.

### Dependencies

This project has no dependencies.

## Primary Features

**World Model:** Tom has a world model, which contains all of his knowledge of the world, when given new information he changes what he knows. Tom's knowledge resets after every conversation. The model contains a list of all the people Tom knows, to start it is just you and him, and the list grows as you tell him about more people. The model also contains a list of jobs, places, activities and more. One shortcoming of many chatbots is that they have no sense of the state of the outside world. Tom can listen and understand a variety of inputs.

**NLP:** Tom's NLP comes from 3 TXT files [SMs.txt](res/phrases/SMs.txt), [QMs.txt](res/phrases/QMs.txt), and [myQs.txt](res/phrases/myQs.txt) in the `res/phrases/` directory.

* **SMs.txt** contains many sample user statements, each followed by its associated meaning, in pseudo code. This method allows Tom to interpret a variety of natural language inputs.
* **QMs.txt** contains many sample user questions, each followed by a direction for how Tom should answer, in pseudo code.
* **myQs.txt** contains many questions for Tom to ask the user, each followed by the corresponding answer that Tom is looking for, in pseudo code. myQs.txt is a subset of QMs.txt

**Sentence Generation:** Tom generates sentences the opposite way that he interprets them. When Tom generates sentences, we starts with an intended meaning he wants to get across and picks a sentence that corresponds to that meaning.
