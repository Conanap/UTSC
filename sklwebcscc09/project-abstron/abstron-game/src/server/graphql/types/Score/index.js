const Score = `
type Score {
    username: String!
    score: Int!
}

type Query {
    highScores(num: Int): [Score]
}

type Mutation {
    setUserHighScore(username: String!, score: Int!): Score
}
`;

module.exports = Score;