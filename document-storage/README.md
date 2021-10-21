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
  * Remove all personal data for a specific user. 
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

### Development time
* 2021-10-20: analysis, project structure (30m).
* 2021-10-21: model (1h).

