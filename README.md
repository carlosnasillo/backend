# lattice-backend

**Will write instructions**

## Installing and running

#### Installation

Need `eris-db` to run blockchain.

Need `node.js` to run server. Install latest + npm (included in standard installation).

Pull in the repo and cd into root, then `npm install`, `npm update`.

#### Running

Start the blockchain client in the folder where the blockchain data files are (`genesis.json`, `priv_validator.json`, etc.). This is done by cd'ing in to that folder and running `erisdb .`. You may also do it from somewhere else e.g. `erisdb /home/users/wherever/the/data/is`.

Start the server from the project root by doing `./bin/lattice.js`. It will start listening on `localhost:3000`.

If it's the **first time** running:
 
- It is good to copy the files out of `chaindata` into a temp folder, since running `erisdb` there will modify the files. `chaindata/temp` is reserved for that if You want to use (it's git ignored). Later, to clear the chain just delete everything in that temp folder and copy in the chaindata files again.

- cd into project root and run `node deploy.js` to add the contract onto the chain.