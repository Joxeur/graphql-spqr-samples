import { GraphQLClient } from 'graphql-request';
import {getSdk} from './generated/graphql'; // THIS FILE IS THE GENERATED FILE

async function main(): Promise<void> {
	const client = new GraphQLClient('http://localhost:8000/graphql');
	const sdk = getSdk(client);

	const createResult = await sdk.createProjectAndTask({projectcode: 'ABAB'});
	console.log(`GraphQL data:`, createResult);

	const project = await sdk.findProject({code: createResult.createProject.code});
	console.log(`GraphQL data 2:`, project);

	return null;
}

main();
