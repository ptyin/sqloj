def insert_one_document(collection, document):
    try:
        collection.insert_one(document)
        return True
    except Exception as e:
        print("An exception occurred ::", e)
        return False


def delete_many_document(collection, doc_filter):
    try:
        collection.DeleteMany(doc_filter)
        return True
    except Exception as e:
        print("An exception occurred ::", e)
        return False


def update_one_document(collection, doc_filter, update):
    try:
        if collection.find_one_and_update(doc_filter, update) is not None:
            return True
    except Exception as e:
        print("An exception occurred ::", e)
    return False
