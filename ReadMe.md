_## Artificial Intelligence
#### [Lab 2]


### Running the code

console
usage:  [-v] [-mode MODE] [-input INPUTFILEPATH] 

Propositional logic solver

positional arguments:
input  Path to the input file

optional arguments:
  -v          Enable verbosity for program runs
  -mode MODE  Mode to run the program. One of cnf, dpll, solver.
  -input INPUTFILEPATH takes path as input


#### Executing the programs

Compile the files using command line (in the folder and unzipped)
javac -classpath "*:." *.java

To run the program in cnf mode with verbose, please use the command
java -classpath "*:." TreeNode -v -mode cnf -input /Users/asishaddepalli/Desktop/example1.txt

To run the program in cnf mode without verbose, please use the command
java -classpath "*:." TreeNode -mode cnf -input /Users/asishaddepalli/Desktop/example1.txt

I am not able to complete DPLL mode.


NOTE: Collaborated with prasanna chiddire (pkc4609)_

