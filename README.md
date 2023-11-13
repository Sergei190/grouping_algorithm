# Grouping Program

This program is designed to group strings from a file based on a given criterion. It partitions unique strings into groups according to the
criterion and outputs the grouped strings.


## Table of Contents

* Introduction
* Installation
* Usage
* Example
* Contributing
* License


## Introduction

The Grouping Program provides a convenient way to categorize and group strings from a file based on a specified criterion. It is useful in
various scenarios where you have a large dataset of strings and want to analyze or process them in a grouped manner.


## Installation

1. Clone the repository.
2. Ensure you have Java Development Kit (JDK) installed on your system.
3. Compile the `GroupingProgram.java` file using the command: `javac GroupingProgram.java`.


## Usage

To use the program, follow these steps:

1. Prepare a text file containing the strings you want to group. Ensure each string is on a separate line.
2. Run the program using the command: `java GroupingProgram <input_file> <criterion>`.
* `<input_file>`: Specify the path to the input file.
* `<criterion>`: Specify the criterion based on which the strings will be grouped.


## Example

Suppose we have a file named data.txt with the following content:

apple
banana
apple
orange
banana
grape

We want to group the strings based on their length. Running the program with the following command:

java GroupingProgram data.txt length

will generate the following output:

Group 1:
banana
orange

Group 2:
apple
apple
grape