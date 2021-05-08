const { mergeTypes } = require("merge-graphql-schemas");

let User = require('./User');
let Score = require('./Score');

const TypeDefs = [User, Score];

module.exports = mergeTypes(TypeDefs, {all: true});