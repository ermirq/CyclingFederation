# MongoDB Initialization Script

## Description

This project provides a Python-based solution to initialize a MongoDB database with initial configuration and data. The script reads configuration and data files from specified directories and inserts the data into the respective MongoDB collections.

## Key Characteristics

- **Flexible Configuration**: Supports configuration files in both JSON and YAML formats.
- **Automated Data Insertion**: Automatically reads and inserts data into MongoDB collections if they are empty, ensuring idempotency.
- **Logging**: Includes detailed logging to track the progress and identify issues during the execution of the script.
- **Error Handling**: Robust error handling for reading files and interacting with MongoDB, enhancing reliability and ease of debugging.

## Usage

1. Place your configuration and data files in the appropriate directories.
2. The script will connect to MongoDB and insert the data as specified in the configuration and data files.

## Configuration

The script can read configuration either from environment variables or from a configuration file (`config.json` or `config.yaml`) located in the `./config` directory.

### Environment Variables

Ensure the following environment variables are set:

- `MONGO_URI` (e.g., `mongodb://mongodb:27017`)
- `MONGO_INITDB_ROOT_USERNAME`
- `MONGO_INITDB_ROOT_PASSWORD`
- `MONGO_INITDB_DATABASE`

### Configuration File

If environment variables are not fully set, the script will read from `./config/config.json`. Example content for `config.json`:

```json
{
  "mongodb_uri": "mongodb://mongodb:27017",
  "username": "root",
  "password": "example",
  "database_name": "testdb"
}
```

### Data Files
Place your data files in the ./data directory. Each data file should be in JSON format and named according to the MongoDB collection it will be inserted into. For example:

users.json for the users collection
products.json for the products collection

#### Example Data File Content
```json
[
  {
    "username": "alice",
    "email": "alice@example.com",
    "password": "alicepassword"
  },
  {
    "username": "bob",
    "email": "bob@example.com",
    "password": "bobpassword"
  }
]
```
