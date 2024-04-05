import { create } from 'zustand';

const useStore = create((set)=>({
    exampleAtom: 0,
    increaseExampleAtom: ()=> set((state)=>({exampleAtom:state.exampleAtom+1})),
    removeAllExampleAtom: ()=>set({exampleAtom:0}),
    updateExampleAtom: (newExampleAtom)=>set({exampleAtom:newExampleAtom}),
}));

export {
    useStore,
}
/*
using sample
function BearCounter() {
    const bears = useStore((state) => state.bears)
    return <h1>{bears} around here...</h1>
}

function Controls() {
    const increasePopulation = useStore((state) => state.increasePopulation)
    return <button onClick={increasePopulation}>one up</button>
}
*/