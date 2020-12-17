

# Mark(ov) My Words!

### Intro
Markov Chains are a great thought experiment
### Endpoint
The most important part of our API our exposed endpoint.  Below, you will find the documentation of generating a chain,     
including the URL, parameters and response.

* **URL**

  /upload

* **Method:**

  `POST`

* **Data Params**

  `file=[File]`(required) The document containing our source text that will be transformed.  Must be of extension `.txt`    
  `n=[Integer]`(optional) Size of "ngram", or count of words that will be grouped together to form a prefix.  Defaults to `1`.

  `length=[Integer]`(optional) Maximum number of words in our resulting Markov transformation.  If not set, algorithm     
  will continue to run until it chooses the last word in the source text.

  `n=[Integer]`(optional) Size of "ngram", or count of words that will be grouped together to form a prefix.  Defaults to `1`.

  `prefix=[String]`(optional) The first substring that will appear in our Markov transformation.  Must contain an actual prefix from the source text.  Also, must contain `n` number of words.  If not set, the default prefix will be the first `n` words in our source text.

* **Success Response:**

  **Code:** 200 <br />    
  **Content:** `{    
  "original": "One black beetle bled only black blood but the other black beetle bled blue\n", "result": "One black blood but the other black blood but the other black beetle bled blue" }`
* **Error Response Example:**

  **Code:** 400 BAD REQUEST <br />    
  **Content:** `{"type": "BadParameterException",    
  "message": "Sorry, one of your parameters was invalid.  Please make sure you have a 'file' field of type 'File' that contains at least one word.", "status": "BAD_REQUEST" }`

  **Sample Call:**

  ```curl --location --request POST 'localhost:8080/upload' \ --form 'file=@/Users/ekudler/Downloads/beetle.txt' \ --form 'n=2' \ --form 'length=8' \ --form 'prefix=the other'```

### Components
MarkMyWords can basically be broken down into 3 parts: the *controller*, the services, and the error handling.  Since we are building a RESTful API, the controller only needs to serve an endpoint that receives parameters and sends back the result.  The *services* are designed to help the controller handle any business logic- such as generating the Markov Chain - without needing any data on our server's state.  If we wanted to, we could even generate chains using other     
input sources, without needing to change the MarkovService class at all.  Finally, we have the exceptions.  Normally in an application, we catch exceptions inside of the methods that invoke Throwables.  However, when the user only has access to one entrypoint in our application, it doesn't really matter much to them what piece failed.  As such, in this project we use Spring's `@RestControllerAdvice` to create a central class for catching exceptions and turning them into  JSON response.

### Things to do
Unfortunately, as with many projects, there were some cool features that I didn't come up with until it was too late to     
safely apply them.  So, I wanted to make a note in this document of a bunch of other ideas I had and hope to add in the     
future:
* **UI:** The great thing about RESTful applications is that they make the frontend totally backend agnostic.  So, since I didn't have enough Photoshop experience to make mock ups, I thought it would be fun to build out a complete UI in React.  Unfortunately I didn't have time to integrate it into my Java build, but I did enclose pictures below!
  ![Sample MarkMyWords UI](https://i.imgur.com/C1xoFuh.pngg)  
  You can also check out the code itself on GitHub [here](https://github.com/Joeento/markmywords-client).

* **Threading/JDBC:**  This may be beyond the scope of the project, but I'm very curious what would happen if you had an impossibly long source text.  For example, what if your text file included an entire Harry Potter book?  Would the result make more or less sense?  Unfortunately, I am not sure this version of MarkMyWords could handle a file over 100MB gracefully.  One idea I had to improve on it was to abstract the Markov algorithm into a thread that runs independent of the main thread.  Basically, on upload, the file and parameter are saved in a jobs table in the database and the client is given a job_id.  Eventually, the table row is enqueued into a thread that runs independent of the main thread and processes the source text at its own speed.  While it is processing, the client can use the job_id to query progress, as well as get the result when the thread is complete, whether that's 30 seconds later or 2 days.
* **Suffix Size:** There are so many different ways you can process a Markov Chain, but one idea I had, I wasn't able to find any examples of anywhere on the internet.  What if, instead of building a table where an n-sized prefix points to a one word suffix, we build one where *an n-sized prefix points to an m-size suffix*?  This way, we can customize our response to any prefix to be varying degrees.  As long as m>n, we will still end up with an unpredictable result.
* **Testing** Testing a Markov Chain can be difficult because the results are by definition, random.  There is no guaranteed input or output.  That said, we can confirm that a given string is a valid Markov Chain of another string.  We would do this by creating a prefix table the same way we do when generating the chain, but instead of creating a new string out of it, we iterate through every `n` words in our test chain and confirm they are followed by an appropriate suffix.  Since we abstracted our logic into a service, this can be easily unit tested without having to simulate uploading a file.  I also would have liked to include some use case-based tests to confirm that exceptions are being thrown in the right places.
* **Optimization:** One last theory I had was built around improving our time complexity.  Even though the application runs quickly, the Big O-notation is very large due to all of the String manipulation involved in generating text.  In the future, I would like to update our prefixTable to replace String key with a LinkedList of words.  In the scenario, we would only need to tokenize our text once, and iterating through our source text could be done in constant time via removing the head and appending to the tail.

### References
For further reference, please consider these documents I used for help:

* [Official Gradle documentation](https://docs.gradle.org)
* [Markov Chains explained visually](https://setosa.io/ev/markov-chains/)
* [Markov Chain Algorithm](https://www.rose-hulman.edu/class/csse/csse221/200910/Projects/Markov/markov.html)