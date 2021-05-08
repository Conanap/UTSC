import React from "react";
import TitleBar from "./titleBar";
import Main from "./main";
import ApolloClient from "apollo-boost";
import { ApolloProvider } from "react-apollo";

const client = new ApolloClient({
  uri: "http://localhost:3030/graphql"
});
const App = () => (
  <React.Fragment>
    <ApolloProvider client={client}>
    <TitleBar />
    </ApolloProvider>
    <Main />
  </React.Fragment>
);

export default App;
