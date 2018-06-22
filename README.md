# Tom-Chatbot

This is a personal project, a rule-based chatbot. Primarily made in 2017, I refactored somewhat in 2018. The only purpose Tom is designed to fulfill is to be capable of a simple chat.

### Getting Started

This project is completely self-contained and has no dependencies.
Simply run `AI_Tom.java` to have a chat.

### Primary Features

World Model: Tom has a "complex" world model, when given new information he changes what he knows. The model contains a list of all the people Tom knows, to start it is just you and him, and the list grows as you tell him about more people. The model also contains a list of jobs, places, activities and more. One shortcoming of many chatbots is that they have no sense of the state of the outside world. Tom can listen and understand a variety of inputs.

NLP: Tom's NLP works by matching user input to sample phrases. These sample phrases exist under the `res\phrases\` path. Each sample phrase, in these files, is followed by its associated meaning. This method allows Tom to interpret a variety of natural language inputs.

Sentence Generation: Tom generates sentences the opposite way that he interprets them. When Tom generates sentences, we starts with an intended meaning he wants to get across and picks a sentence that matches the intended meaning.

### Author

Kevin Adams
