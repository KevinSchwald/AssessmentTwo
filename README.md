# AssessmentTwo

Given is a csv file containing the gas consumption of a customer. In the file are monthly records of gas consumption.
A record contains the beginning date, the end date, initial counter reading (Begin_indx), final counter reading (End_indx), gas conversion ratio (kpcs).

The consumption per month in m³ is calculated as follows: consumption m³ = End_indx-Begin_indx.

The consumption per month in kWh is calculated as follows: consumption kWh = Round(consumption m³*kpcs).

Tasks:

Calculate the monthly consumption in m³ and in kWh.

Calculate per year: the total consumption, the maximum consumption, the maximum consumption month, the minimum consumption, the minimum consumption month, and the consumption average.

Create a User Interface in the console that prints consumption information about a specific month and/or year.

Write JUnit tests to test the implemented functionality.

Maximum Points: 25

Requested Functionality (max 14 Points)
Classes for monthly consumption and yearly consumption (4 P)
Correct calculations (5 P)
User Interface (3 P)
File handling & processing (2 P)
Additional Features (max 2 Points)
e.g. Pretty print functions for the results Test cases (max 5 Points)
Code style etc. (max 4 Points)
code style (no warnings, errors, TODO or unused code) (1 P)
Comments for all public components (1 P)
Class/function design (1 P)
Kotlin style (nullable and mutable types should be avoided, correct val/var usage, no Any? type, ...) (1 P)
Individual task processing (no teamwork!)
