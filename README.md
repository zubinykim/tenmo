# TEnmo

## Use cases

### Required use cases

You should attempt to complete all of the following required use cases.

1. As a user of the system, I need to be able to register myself with a username and password.
   1. The ability to register has been provided in your starter code.
2. As a user of the system, I need to be able to log in using my registered username and password.
   1. Logging in returns an Authentication Token. I need to include this token with all my subsequent interactions with the system outside of registering and logging in.
   2. The ability to log in has been provided in your starter code.
   3. User ids start at 1001.
3. As a user, when I register a new account is created for me.
   1. The new account has an initial balance of $1000.
   2. Account ids start at 2001.
4. As an authenticated user of the system, I need to be able to see my Account Balance. 

**Endpoint Specification:** 
```
Create an endpoint mapping with a suitable path (i.e. /api/something) and request type. 
The request should only contain a token. 
No id's or usernames should be present on the request.
```

**Output:**
The response should be in the following format:
```
{
   "username" : "user",
   "balance" : 1000
}
```

5. As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
   1. I need an endpoint that shows the users I can send money to.
   2. I must not be allowed to send money to myself.
   3. A transfer includes the username of the recipient and the amount of TE Bucks.
   4. The receiver's account balance is increased by the amount of the transfer.
   5. The sender's account balance is decreased by the amount of the transfer.
   6. I can't send more TE Bucks than I have in my account.
   7. I can't send a zero or negative amount.
   8. A Sending Transfer has an initial status of *Approved*.
   9. Transfer ids start at 3001.

**Hint:** It's likely that you will have to design a new table to support
this feature.

**Endpoint Specification (for 5.i):**
```
Create an endpoint mapping with a suitable path (i.e. /api/something) and request type. 
The request should only accept a token. 
No id's or usernames should be present on the request.
```

**Output (for 5.i):**
The response should be in the following format:
```
[
   { "username" : "Alice"},
   { "username" : "Bob"},
   { "username  : "Carly"}
]
```

**Endpoint Specification (for 5.iii):**
```
Create an endpoint with a suitable path and request type.
This request should support a request body. The body should
only contain the amount and recipient name.
```

**Output (for 5.iii):**
The response should be in the following format:
```
{
   "transferId": 1,
   "transferAmount" : 125,
   "from" : "Carly",
   "to": "Bob"
}
```
**A successful response must have a status code of 201 Created**

6. As an authenticated user of the system, I need to be able to see transfers I have sent or received.

**Endpoint Specification:**
```
Create an endpoint mapping with a suitable path (i.e. /api/something) and request type. 
The request should only accept a token. No id's or usernames should be present on the request.
```

**Output:**
The response should be in the following format:
```
[
   {
      "transferId": 1,
      "transferAmount" : 125,
      "from" : "Carly",
      "to": "Bob"
   }, 
   {
      "transferId": 2,
      "transferAmount" : 10,
      "from" : "Carly",
      "to": "David"
   }
]
```
7. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.

**Endpoint Specification:**
```
Create an endpoint mapping with a suitable path (i.e. /api/something) and request type. 
The request should contain a token, and no account or user id's should be present. 
However, in this case, you may include a path variable that represents the transfer ID.
```
**Output:**
The response should be in the following format:
```
{
    "transferId": 2,
    "transferAmount" : 10,
    "from" : "Carly",
    "to": "David"
}
```

## Testing

Validate all of the API's endpoints using Postman.  *No sensitive information (i.e. account numbers, user ids, etc) should be passed in the URL.*  Integration testing is required on each method that connects to the database.

---

## Database schema

The following tables are created by the provided `tenmo.sql` script. **The 
CREATE TABLE statements for any new tables you chose to create should be in this script.**

### `tenmo_user` table

Stores the login information for users of the system.

| Field           | Description                                                                    |
| --------------- | ------------------------------------------------------------------------------ |
| `user_id`       | Unique identifier of the user                                                  |
| `username`      | String that identifies the name of the user; used as part of the login process |
| `password_hash` | Hashed version of the user's password                                          |

### `account` table

Stores the accounts of users in the system.

| Field           | Description                                                             |
| --------------- | ----------------------------------------------------------------------- |
| `account_id`    | Unique identifier of the account                                        |
| `user_id`       | Foreign key to the `tenmo_user` table; identifies user who owns account |
| `balance`       | The amount of TE bucks currently in the account                       |

