
Users:

```json
{
    "userId" : "abc123", //UUID
    "userName" : "Jean-Michel", //String
    "birthDate" : "01/02/2003", //Date
    "location" : "France", //String
}
```



Posts:

```json
{//Si repostOf et replyTo null => Post | repost et reply ne peuvent pas être non null en même temps
  "postId": "abc123",  //UUID
  "authorId": "user123", //UUID
  "content": "MongoDB est génial ! #NoSQL #Quarkus",
  "hashtags": ["BouhouLaHonte123", "Ratio"], // Nécéssite un parsing supplémentaire (ne peux contenir que lettres chiffres et underscore)
  "mediaUrl": "https://image.jpg", // A voir comment ça fonctionne avec l'upload etc
  "repostOf": null, // Si non null => Repost
  "replyTo": null, // Si non null => commentaires
  "createdAt": "2025-03-04T12:00:00Z" //TimeStamp classique
}
```

Likes:
```json
{
  "userId": "user123", //UUID
  "postId": "abc123", //UUID
  "likedAt": "2025-03-04T12:05:00Z" //TimeStamp
}
```

Follows:
```json
{
  "followerId": "user123", 
  "followedId": "user456",
  "followedAt": "2025-03-04T12:10:00Z"
}
```

Block's user:
```json
{
  "blockerId": "user123",
  "blockedId": "user789",
  "blockedAt": "2025-03-04T12:15:00Z"
}
```

User Timeline
```json
{// Historique de l'utilisateur de ses posts / likes / repost
  "userId": "user123",
  "posts": [
    { "postId": "abc123", "hashtags": ["feur", "test"], "createdAt": "2025-03-04T12:00:00Z" } //HashTags a revoir
  ]
}
```

Home Timeline
```json
{ // Historique de ses abonnements de leurs posts / likes / reposts
  "userId": "user123",
  "posts": [
    { "postId": "def456", "authorId": "user456", "hashtags": ["loser"], "createdAt": "2025-03-04T12:30:00Z" } //HashTags a revoir
  ]
}
```

