# AskMe - Social Q&A Platform

## Overview
AskMe is a Java-based console application for a social question-and-answer platform. Users can sign up, log in, ask/answer questions (anonymously or not), delete their questions, and view a feed with threaded responses. Data is stored in text files.

## Features
- User authentication (sign up, sign in)
- Ask questions (anonymous or threaded)
- Answer or delete questions
- View questions to/from you or system feed
- List all users
- Persistent storage in text files

## Project Structure
- **models**: `User`, `Question`, `ThreadedQuestion`, `Answer`
- **services**: `UserServices`, `QuestionServices`, `SessionServices`
- **UI**: `MainMenu` for console interface
- **Utils**: `FileHandler` for file operations

### Data Files
Stored in `./src/main/resources/`:
- `users.txt`: User data
- `questions.txt`: Questions (ID, content, anonymity, answerId, [parentId])
- `answers.txt`: Answers (ID, content)
- `from_to.txt`: Question mappings (fromId, toId, questionId)
- `ids.txt`: Next available IDs

## Prerequisites
- Java Development Kit (JDK) 8+
- IDE (e.g., IntelliJ, Eclipse) or command-line build tools

## Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd AskMe
   ```
2. Ensure `./src/main/resources/` exists.
3. Run `Main.java` via IDE or:
   ```bash
   javac -d . src/**/*.java
   java org.example.Main
   ```

## Usage
1. **Authenticate**: Sign up or sign in.
2. **Main Menu**:
   - `[1] Questions to me`
   - `[2] Questions from me`
   - `[3] Feed`
   - `[4] Answer question`
   - `[5] Delete question`
   - `[6] Ask question`
   - `[7] List users`
   - `[8] Logout`
3. Follow prompts for actions like asking threaded questions or answering.

## Future Improvements
- Add input validation
- Implement GUI or web interface
- Use a database
- Support concurrent sessions
