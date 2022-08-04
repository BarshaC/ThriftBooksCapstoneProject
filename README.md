# ThriftBooksCapstoneProject

Hello World!
ThriftBooks can be useful for people who want to sell, borrow, buy used books. Users can post new books and keep it either for sell or borrowing. People can follow each other and see if the people they are following have posted a new book. App is mainly targeted for the people who generally love reading books and like sharing books.

### App Evaluation
- **Category:** LifeStyle/Educational
- **Mobile:** App can be used on mobile or can also be used on web like most of the apps. Similar features on mobile and web. Mobile has more features.
- **Story:** User post picture of the book for sell, borrow. Also, the user can see posts of other people they are following. 
- **Market:** Can be used by people who can buy or is willing to sell used books.For security reasons, the person should be of certain age category.
- **Habit:** User can use to often or unoften depending on reading habits of a person. 
- **Scope:** People can use it as a platform to sell, give away or buy used books easily. 

## Product Spec

### 1. User Stories 

**Required Must-have Stories Made**

* User register for an account and log in to post books. 
* User picks their favorite genre of book - fictional/non fictional/ educational
* Compose page to post their books 
* User can take picture of book and write all requirements and post it.
* Login using Facebook Login Button

### 2. Screen Archetypes

* Register for account
   * Login
* Login 
   * Confirm email address
* Stream
   * Can see guide post with the example of a posted picture of book (first time user)
   * Click on guide for onboarding (first time user)
   * Compose a post to sell a book
   * Get notify if someone is trying to contact you to buy your book 
* Search Book Screen
  * Search for genres of book or name of books to buy/borrow 
* Profile Screen
   * Make Profile by going into creating profile page
   * Change username (optional)
* Logout Screen
 
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Stream (main screen)
* Profile
* Search Book
* Compose Book/ Post Book 

**Flow Navigation** (Screen to Screen)

* Guided to Login page
   * Register for a account in case user doesn't have 
* Register
   * Login
* Login
   * View the homepage with the posts of others with your matching favorite genre
   * Search for book/gemre
* Search Bar
   * Get Results for books based on Genres
     * For Borrow
       * Top result of book for generous book aka with the most reviews and genre match 
       * Go to the about page of the book 
       * Instant messaging to ask more about the book
       * Borrow book 
     * For sale 
       * Top result of book for good booksellers aka with the most followers and books available in the nearest location
       * Go to the about page of the book
       * Message to ask more about the book 
       * Buy the book
       * Get waitlisted if book not available (Optional)
 * Profile 
   * Visit Profile and edit profile  
 
 #Peak of App
![VideoRunThrough1 mp4](https://user-images.githubusercontent.com/64405568/182931591-621a03b8-cef3-4f03-a546-08e38daea454.gif)


### Networking
*  Login Screen
   * (Read/GET) Get user information based on login information
*  Sign Up Screen
   * (Create/POST) Create a new user object
*  Compose Book
   * (Create/POST) Create a new Book object
*  Profile Screen
   * (Read/GET) Query logged in user object
   * Read/GET) Query books posted by Book object
*  Stream Screen (Home Screen)
   * (Read/GET) List out logged in user's Books based on the fav genre
   * (Create/POST) Go to Compose book and create book
   * (Delete/POST) Delete the book posted 
- Using https://www.back4app.com/ for Parse
### APIs and SDK used
* APIs used
  * Google Books API https://developers.google.com/books/docs/overview
* SDK that might be useful for login/Signup
  * Login with Facebook https://developers.facebook.com/docs/facebook-login/android/
