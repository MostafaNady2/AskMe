
# AskMe - Social Q&A Platform

## Overview
AskMe is a Java-based console application for a social question-and-answer platform. Users can sign up, log in, ask/answer questions (anonymously or not), delete their questions, and view a feed with threaded responses. Data is stored in text files.

🎥 **[Watch Demo Video](https://drive.google.com/file/d/1jgTq-e5fZqUV_AmZO4UjjEc8DFv5mE7I/view?usp=sharing)**  
[![Watch Demo](https://img.shields.io/badge/Watch-Demo-blue?logo=google-drive&style=for-the-badge)](https://drive.google.com/file/d/1jgTq-e5fZqUV_AmZO4UjjEc8DFv5mE7I/view?usp=sharing)

---

## Features
- User authentication (sign up, sign in)  
- Ask questions (anonymous or threaded)  
- Answer or delete questions  
- Overwrite or update existing answers  
- View questions to/from you or system feed  
- Threaded question and answer display  
- List all users  
- Persistent storage in text files  
- Robust input validation  

---

## Project Structure
- **models**: `User`, `Question`, `ThreadedQuestion`, `Answer`  
- **services**: `UserService`, `QuestionService`, `SessionService`  
- **ui**: `MainMenu` for console interface  
- **utils**: `FileHandler`, `InputValidator`, `IdGenerator`  

### Data Files
Stored in `./src/main/resources/`:
- `users.txt`: User data  
- `questions.txt`: Questions (ID, content, anonymity, answerId, [parentId])  
- `answers.txt`: Answers (ID, content)  
- `from_to.txt`: Question mappings (fromId, toId, questionId, isThreaded)  
- `ids.txt`: Tracks next available IDs for users, questions, and answers  

---

## Prerequisites
- Java Development Kit (JDK) 8+  
- IDE (e.g., IntelliJ, Eclipse) or command-line build tools  

---

## Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd AskMe
   ```

2. Run the application once to auto-generate the `./src/main/resources/` folder and necessary files.

3. Run `Main.java` via IDE or:

   ```bash
   javac -d . src/**/*.java
   java org.example.Main
   ```

---

## Usage

1. **Authentication**
   Choose to sign up or sign in using a username and password.

2. **Main Menu**

   ```
   ┌───────────────────────────────────────────┐
   │         === AskMe Main Menu ===           │
   │ [1] Print Questions To Me                 │
   │ [2] Print Questions From Me               │
   │ [3] Print Feed                            │
   │ [4] Answer or Update Answer               │
   │ [5] Delete Your Questions                 │
   │ [6] Ask New or Threaded Question          │
   │ [7] View All Users                        │
   │ [8] Logout and Return to Login            │
   └───────────────────────────────────────────┘
   ```

3. **Threaded Q\&A**

   * When asking, you can specify if the question is anonymous and/or threaded.
   * If threaded, you’ll be prompted for a valid parent question ID.
   * Feed displays threaded follow-ups under parent questions.

4. **Answering Questions**

   * If a question is already answered, you’ll be asked whether to overwrite it.

---

## Notes

* The system auto-creates all required data files on the first run.
* Threaded questions help create conversations under a main topic.
* Data is stored in plain text and loaded in-memory during session runtime.
* File paths are hardcoded; ensure relative paths are preserved when deploying.

---

## Future Improvements

* Input sanitization and stronger validation rules
* GUI or web-based front end
* Replace file storage with an actual database
* Support for concurrent users and sessions

