## Functionality Documentation

## Modules

### 1. User Management

This module implements the access control of users. Specifically, the roles of users can be classified into two
categories - student & teacher.

- login with password validation
- remember me (achieved by cookie mechanism)
- change password
- log out

### 2. Assignments

The main functionality of this project is to implement a joint platform to provide **assignment publishing** & **open
judge**.

Every assignment contains several questions.

#### 2.1 Student

- view assignments and experiments list
- write answers to the questions via a rich text editor
    - open judge of experiments and submit the result to the teacher
    - *submit textual answers to the teacher (to be determined)*
- view the submission records

#### 2.2 Teacher

- show status of the assignments (i.e., name/start/DDL/description)
- update status
- add assignments
    - add questions
    - update questions
    - delete questions
- delete assignments

- add databases by uploading the corresponding sqlite file
    - update databases by overriding the corresponding sqlite file
    - delete databases