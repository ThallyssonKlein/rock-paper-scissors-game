import hashlib

def generate_hash(move, salt):
    text = f"{move}:{salt}"
    hash_bytes = hashlib.sha256(text.encode('utf-8')).digest()
    return hash_bytes.hex()

def verify_hash(move, salt, stored_hash):
    generated_hash = generate_hash(move, salt)
    return generated_hash == stored_hash

# Get inputs from the user
move = input("Enter the move: ")
salt = input("Enter the salt: ")
stored_hash = input("Enter the stored hash: ")

# Verify the hash
if verify_hash(move, salt, stored_hash):
    print("Hash verified successfully!")
else:
    print("Hash does not match.")