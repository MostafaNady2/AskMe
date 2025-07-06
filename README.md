AskMe - Social Question and Answer Platform

Overview

AskMe is a Java-based console application designed to facilitate a social question-and-answer platform. Users can sign up, log in, ask questions (anonymously or not), answer questions, delete their own questions, and view a feed of questions and answers. The application supports threaded questions, allowing users to create follow-up questions linked to existing ones. Data is persisted in text files, simulating a simple file-based database.

Features





User Authentication: Sign up with a name, email, username, and password, or sign in with existing credentials.



Question Management: Ask questions to other users, with options for anonymity and threading.



Answer Management: Answer questions directed to you, with the ability to overwrite existing answers.



Question Deletion: Delete your own questions.



Feed Display: View a feed of all questions and their answers, including threaded responses.



User Listing: List all registered users in the system.



Threaded Questions: Create follow-up questions linked to a parent question.



Persistent Storage: Store user data, questions, answers, and mappings in text files.

Project Structure

The project is organized into three main packages:





models: Contains data models for User, Question, ThreadedQuestion, and Answer.



services: Handles business logic, including user management (UserServices), question and answer management (QuestionServices), and session initialization (SessionServices).



UI: Manages the console-based user interface (MainMenu).



Utils: Provides utility functions for file handling (FileHandler).

Key Files





Main.java: Entry point of the application, initializes the session and displays the main menu.



User.java: Represents a user with attributes like ID, name, email, username, and password.



Question.java: Base class for questions, including content, anonymity, and answer status.



ThreadedQuestion.java: Extends Question to support threaded questions with a parent ID.



Answer.java: Represents an answer with an ID and content.



UserServices.java: Manages user-related operations, such as listing users and retrieving questions sent to or from a user.



QuestionServices.java: Handles question and answer operations, including asking, answering, deleting, and displaying questions.



SessionServices.java: Initializes the in-memory database and handles user sign-in.



FileHandler.java: Manages file operations for reading, writing, and ID generation.



MainMenu.java: Implements the console-based user interface with authentication and option menus.

Data Storage

Data is stored in the ./src/main/resources/ directory in the following text files:





users.txt: Stores user data (ID, name, email, username, password).



questions.txt: Stores question data (ID, content, isAnonymous, answerId, [parentId for threaded questions]).



answers.txt: Stores answer data (ID, content).



from_to.txt: Maps questions to sender and receiver IDs (fromId, toId, questionId, isFollowed).



ids.txt: Tracks the next available IDs for users, questions, and answers.

Prerequisites





Java Development Kit (JDK): Version 8 or higher.



IDE or Build Tool: Any Java-compatible IDE (e.g., IntelliJ IDEA, Eclipse) or build tool (e.g., Maven, Gradle) for running the project.

Setup Instructions





Clone the Repository:

git clone <repository-url>
cd AskMe



Ensure Resource Directory:





Verify that the ./src/main/resources/ directory exists. If not, create it.



The application will automatically create necessary text files (users.txt, questions.txt, answers.txt, from_to.txt, ids.txt) on first run.



Compile and Run:





Open the project in your IDE and run Main.java.



Alternatively, compile and run from the command line:

javac -d . src/**/*.java
java org.example.Main



Interact with the Application:





Follow the console prompts to sign up, sign in, or perform actions like asking/answering questions.

Usage





Authentication:





Choose [1] Sign Up to create a new user account or [2] Sign In to log in with existing credentials.



Main Menu:





After logging in, select from options:





[1] Print Questions To Me: View questions asked to you.



[2] Print Questions From Me: View questions you asked.



[3] Print Feed: View all questions and answers in the system.



[4] Answer Question: Answer a question directed to you.



[5] Delete Question: Delete one of your own questions.



[6] Ask Question: Ask a new question to another user, with options for anonymity and threading.



[7] List System Users: View all registered users.



[8] Logout: Return to the authentication menu.



Threaded Questions:





When asking a question, choose whether it’s threaded and provide a parent question ID if applicable.

Example Workflow





Sign up with a username and password.



List system users to find a user ID to ask a question to.



Ask a question, choosing whether it’s anonymous or threaded.



View questions sent to you or from you.



Answer a question by entering its ID and your response.



View the feed to see all questions, answers, and threaded responses.

Limitations





Console-Based Interface: The application uses a text-based interface, which may not be as user-friendly as a GUI or web-based system.



File-Based Storage: Data persistence relies on text files, which may not scale well for large datasets.



No Input Validation: Limited validation for user inputs (e.g., email format, duplicate usernames).



Single Session: Only one user can interact with the system at a time via the console.

Future Improvements





Add input validation for usernames, emails, and passwords.



Implement a graphical user interface (GUI) or web interface.



Replace file-based storage with a proper database (e.g., SQLite, MySQL).



Support concurrent user sessions.



Add search functionality for questions and users.



Enhance threading with deeper nesting and better visualization.

License

This project is licensed under the MIT License. See the LICENSE file for details.

Contact

For questions or contributions, please contact the project maintainer at [your-email@example.com].
