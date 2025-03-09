#Ce fichier sert a generer un fichier JSON de Test pour le projet
#%%
from pymongo import MongoClient
import uuid
import random
import datetime
from bson import Binary

userNbr = 1000
postNbr = 1000

#generate n user with random uuid, random username, randomer date of birth and random Country
def generate_random_date():
    
    delta = datetime.datetime(2005, 12, 31) - datetime.datetime(1950, 1, 1) 
    days = random.randint(0, delta.days)
    return (datetime.datetime(1950, 1, 1)  + datetime.timedelta(days=days))


def generateUser(n):
    first_names = [
        "Alice", "Bob", "Chloé", "David", "Emma", "François", "Géraldine", "Hugo", "Isabelle", 
        "Jacques", "Katherine", "Léa", "Maxime", "Nicolas", "Olivia", "Pierre", "Quentin", 
        "Rachel", "Sophie", "Thomas", "Ursula", "Victor", "Wendy", "Xavier", "Yasmine", "Zoe"
    ]
    users = []
    for i in range(n):
        user = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "userName": random.choice(first_names) + str(random.randint(1, 100)),
            "birthDate": generate_random_date(),
            "location": random.choice(["France", "Germany", "Italy", "Spain", "United Kingdom", "United States"])
        }
        users.append(user)
    return users

def generatePost(n, users):
    posts = []
    words = [
        "papillon", "voiture", "océan", "montagne", "maison", "arbre", "ordinateur", "livre", 
        "ciel", "bouteille", "clé", "voix", "soleil", "fleur", "chat", "chien", "pomme", 
        "étoile", "brouillard", "nuage", "lune", "parapluie", "corde", "musique", "fleur", 
        "montre", "papier", "souris", "téléphone", "coeur", "glace", "porte", "balle", "rivière"
    ]
    for i in range(n):

        #genere moi des hashtags aleatoire a partir de words
        hashtags = random.sample(["#"+word for word in words], random.randint(1, 2))

        post = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "userId": random.choice(users)["_id"],
            "content": " ".join(random.sample(words, random.randint(5, 10))) + " " + " ".join(hashtags),
            "hashtags" : hashtags,
            "mediaUrl" : None,
            "repostOf" : None,
            "replyTo" : None,
            "createdAt": datetime.datetime.now()
        }
        posts.append(post)
    return posts

def generateComments(n, posts, users):
    comments = []
    words = [
        "papillon", "voiture", "océan", "montagne", "maison", "arbre", "ordinateur", "livre", 
        "ciel", "bouteille", "clé", "voix", "soleil", "fleur", "chat", "chien", "pomme", 
        "étoile", "brouillard", "nuage", "lune", "parapluie", "corde", "musique", "fleur", 
        "montre", "papier", "souris", "téléphone", "coeur", "glace", "porte", "balle", "rivière"
    ]

    for i in range(n):
        hashtags = random.sample(["#"+word for word in words], random.randint(1, 2))
        comment = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "userId": random.choice(users)["_id"],
            "content": " ".join(random.sample(words, random.randint(5, 10))) + " " + " ".join(hashtags),
            "hashtags" : hashtags,
            "mediaUrl" : None,
            "repostOf" : None,
            "replyTo" : random.choice(posts)["_id"],
            "createdAt": datetime.datetime.now()
        }
        comments.append(comment)
    return comments
def generateReposts(n, posts, users):
    reposts = []

    for i in range(n):
        repost = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "userId": random.choice(users)["_id"],
            "content": None,
            "hashtags" : None,
            "mediaUrl" : None,
            "repostOf" : random.choice(posts)["_id"],
            "replyTo" : None,
            "createdAt": datetime.datetime.now()
        }
        reposts.append(repost)
    return reposts

def genereteLikes(n, users, posts):
    likes = []
    for i in range(n):
        like = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "userId": random.choice(users)["_id"],
            "postId": random.choice(posts)["_id"],
            "likedAt": datetime.datetime.now()
        }
        likes.append(like)
    return likes

def generateFollows(n, users):
    follows = []
    for i in range(n):
        follow = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "followerId": random.choice(users)["_id"],
            "followedId": random.choice(users)["_id"],
            "followedAt": datetime.datetime.now()
        }
        follows.append(follow)
    return follows

def generateBlocks(n, users):
    blocks = []
    for i in range(n):
        block = {
            "_id": Binary.from_uuid(uuid.uuid4()),
            "blockerId": random.choice(users)["_id"],
            "blockedId": random.choice(users)["_id"],
            "blockedAt": datetime.datetime.now()
        }
        blocks.append(block)
    return blocks

#%%
userNbr = 1000
postNbr = 2500
repostNbr = 1000
commentNbr = 1500
likeNbr = 2000
followNbr = 500
blockNbr = 100

users = generateUser(userNbr)
posts = generatePost(postNbr, users)
reposts = generateReposts(repostNbr, posts, users)
comments = generateComments(commentNbr, posts, users)
likes = genereteLikes(likeNbr, users, posts)
follows = generateFollows(followNbr, users)
blocks = generateBlocks(blockNbr, users)

client = MongoClient("mongodb://localhost:27017/")
db = client["tinyX"] 
db["users"].insert_many(users)
db["posts"].insert_many(posts)
db["posts"].insert_many(reposts)
db["posts"].insert_many(comments)
db["likes"].insert_many(likes)
db["follows"].insert_many(follows)
db["blocks"].insert_many(blocks)


# %%
