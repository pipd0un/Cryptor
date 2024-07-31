# Cryptor

- Sometimes encryption of the customs declaration is essential for some countries.
- And this is for it.

### How to use?
- Need to create a cert/ dir under py/
- Need to place your xml declaration & .pem under java/main/resources/
- Then rename the dropdownbox menu items' names in gui.py .ex "Halcom.pem" rename the item's name as "Halcom".

### How to build?

#### WINDOWS
- It's okay to run the batch file

#### LINUX & MAC
- You need to have maven's latest release and (may want to use Intellij IDEA) then build from scratch

##### -Probable FAQ-
- Why cross-compiled? <br>
  Because core encryption process' in java are relatively more mature then others. <br>
  and I did not wanted to hard-on writing gui with java so go with python for it.
