import json
import os
import yaml
import logging
from pymongo import MongoClient, errors

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def read_config(config_file):
    logging.info(f"Reading configuration file: {config_file}")
    try:
        with open(config_file, 'r') as file:
            if config_file.endswith('.json'):
                config = json.load(file)
            elif config_file.endswith('.yaml'):
                config = yaml.safe_load(file)
            else:
                raise ValueError("Unsupported config file format. Use .json or .yaml")
        logging.info("Configuration file read successfully")
        return config
    except Exception as e:
        logging.error(f"Failed to read configuration file: {e}")
        raise

def get_config():
    config = {
        'mongodb_uri': os.getenv('MONGO_URI'),
        'username': os.getenv('MONGO_INITDB_ROOT_USERNAME'),
        'password': os.getenv('MONGO_INITDB_ROOT_PASSWORD'),
        'database_name': os.getenv('MONGO_INITDB_DATABASE'),
    }
    
    if all(config.values()):
        logging.info("Using configuration from environment variables")
        return config
    else:
        logging.info("Environment variables not fully set, reading from config file")
        config_file = './config/config.json'
        return read_config(config_file)

def read_data_file(data_file):
    logging.info(f"Reading data file: {data_file}")
    try:
        with open(data_file, 'r') as file:
            data = json.load(file)
        logging.info("Data file read successfully")
        return data
    except FileNotFoundError:
        logging.error(f"Data file not found: {data_file}")
        raise
    except json.JSONDecodeError:
        logging.error(f"Error decoding JSON from file: {data_file}")
        raise

def insert_data(db, collection_name, data):
    collection = db[collection_name]
    try:
        if collection.count_documents({}) == 0:
            collection.insert_many(data)
            logging.info(f"Data inserted into {collection_name}")
        else:
            logging.info(f"{collection_name} is not empty, skipping insertion")
    except errors.PyMongoError as e:
        logging.error(f"Error inserting data into {collection_name}: {e}")
        raise

def main():
    config = get_config()

    try:
        client = MongoClient(config['mongodb_uri'], username=config['username'], password=config['password'])
        db = client[config['database_name']]
        logging.info("Connected to MongoDB successfully")
    except errors.ConnectionError as e:
        logging.error(f"Failed to connect to MongoDB: {e}")
        raise
    
    data_dir = './data'
    data_files = [f for f in os.listdir(data_dir) if f.endswith('.json')]
    
    for data_file in data_files:
        collection_name = data_file.split('.')[0]
        data_file_path = os.path.join(data_dir, data_file)
        data = read_data_file(data_file_path)
        insert_data(db, collection_name, data)

if __name__ == "__main__":
    main()