# Document Storage

Simple service to store users' documents

### Requirements
* Java. 
* Users can have 0+ documents.
* Document has metadata (name, type, ...). 
* Document names are unique for a given user. 
* REST endpoints for documents: 
  * upload
  * download
  * metadata modification
  * get all documents for given user
* Tests. 
  * Get all documents for existing user.
  * Upload a new document. 
  * Upload a document multiple times. 
  * Download a specific document. 
  * Update document metadata. 
  * Get all documents for non-existing user. 
* Concurrency. 
* Scalability.
* Atomicity. 
* Idempotency. 

### Analysis
* Document
  * ID
  * created
  * deleted
  * user_id
  * name
  * relative_file_path
  * type
  * notes
  * ...

Initial thoughts
* Spring Boot. 
* H2 database.
* For DEMO purpose, create a static set of users. 
* Do not store binary documents in database. 
* Use generated names for the stored files (paths are in database). 
* Security (encryption). 
* No deletes, only updates. 
* Option to remove all personal data, while keeping the database structure. 

### What is not implemented
* Using permanent database.
* Repository can return single document instead of list, then controller logic becomes easier. 
* Default page ("/"). 
* Error pages (400, 500).
* Authentication (e.g. CAS, OAuth2, ...). 
  * A token can identify a user (userId) => then the endpoints do not need userId parameter. 
* Procedure to remove all personal data while keeping database structure in tact (GDPR).
* Encryption of the uploaded files. 
* DocumentRepository.updateUserDocument(document). Is there a way how to use object notation in the "sql"?
* Check the file size (allow certain limit) and type (PDF, doc(x), jpg, ...).
* Directory for file storage: checking its existence, structure based on year/month/day (for example).

### Development time
* 2021-10-20: analysis, project structure (30m).
* 2021-10-21: model (1h).
* 2021-10-23: repository, controller, REST API, file upload, file storage service (5h30m)
* 2021-10-24: download, refactoring (45m).

