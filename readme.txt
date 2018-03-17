1. Open Command Prompt

2. cd to the current folder

3. Enter "javac BackPackMain.java" to compile the code

4. Enter "java BackPackMain" to run the program

5. When prompted for the data file name, enter "data.txt"


-------------------- Program Description --------------------

The continuous backpack problem in
which a backpack with capacity W is to be filled with items selected from a
set of n objects each with a weight wi and a value vi in such a way that the
backpack is not overfilled and the value of the items in the backpack is
maximised. There exists an easy solution to this problem provided we are
allowed to put part of an object into the backpack.

If we impose an additional restriction that we must put all of an object into
the backpack, we have the discrete backpack problem which is much
harder to solve.

We can solve this problem by using a branch-and-bound approach where, at
each stage we use the solution to the continuous backpack problem to
establish an upper bound on the value of the remaining contents.
This program implement a program which will solve both of these problems.

Input to the program will consist of the name of a data file.
This file will contain the following data:
• The capacity of the backpack: W
• The number of items: n
• The weight and value of each item: wi vi

Output will consist of:
• The optimum value of the backpack
• For each item, in order, the proportion of the item in the backpack, its
weight and its value

This output should be provided, first for the continuous problem and
then for the discrete problem.

NOTE: The proportion of each item will be a value between 0 and 1 in the
continuous problem and will be exactly 0 or 1 in the discrete problem.