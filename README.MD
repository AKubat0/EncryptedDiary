#Encrypted Diary Manager

A Java-based command-line application designed for managing personal diary entries. This tool provides a local storage solution where entries are automatically encrypted before being saved to the file system.

##Features
Entry Creation: Write and save diary entries that are automatically timestamped.

Basic Encryption: Implements a Caesar Cipher to obfuscate stored text files.

Retrieval and Decryption: Read past entries by date with automatic decryption.

Management: List all saved entries with file metadata or delete specific files with a confirmation prompt.

Search: Search for specific keywords across all encrypted entries.

##Technical Implementation
The application is built using standard Java libraries:

File I/O: Utilizes java.io and java.util.Scanner for file management and user input.

Cryptography: Implements a character-shift algorithm (Caesar Cipher) with a fixed key.

Data Formatting: Uses java.time for consistent date-based file naming and metadata display.

##Installation and Usage
Requirements: Ensure Java Development Kit (JDK) is installed on your system.

##Compilation:

Bash
javac EncryptedDiaryManager.java
Execution:

Bash
java EncryptedDiaryManager
Directory Structure
EncryptedDiaryManager.java: The primary source code.

diary_entries/: A local directory created at runtime to store the encrypted .txt files.

Security Disclaimer
This project is intended for educational purposes. The Caesar Cipher is a simple substitution cipher used here to demonstrate basic string manipulation and file security logic. It is not suitable for securing sensitive or highly confidential information.
