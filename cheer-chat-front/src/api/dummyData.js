
const dummyUserProfile = {
    name:"username",
    id: 0,
    profile:"https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
}
const dummyChatData=[
    {
        id: 4,
        name: "user01",
        profile: "https://cdn.pixabay.com/photo/2016/11/18/12/14/owl-1834152_1280.jpg",
        message: "this is test message",
        timestamp: "2022-01-01",
        notice: 5,
    },
    {
        id: 2,
        name: "user02",
        profile: "https://cdn.pixabay.com/photo/2016/11/18/12/14/owl-1834152_1280.jpg",
        message: "this is test message",
        timestamp: "2021-12-01",
        notice: 4,
    },
    {
        id: 1,
        name: "user03",
        profile: "https://cdn.pixabay.com/photo/2016/11/18/12/14/owl-1834152_1280.jpg",
        message: "this is test message",
        timestamp: "2021-11-01",
        notice: 3,
    },
    {
        id: 0,
        name: "user04",
        profile: "https://cdn.pixabay.com/photo/2016/11/18/12/14/owl-1834152_1280.jpg",
        message: "this is test message",
        timestamp: "2021-10-01",
        notice: 2,
    }
];
const logoImg="https://cdn.pixabay.com/animation/2023/12/22/00/19/00-19-58-407_512.gif"
const defaultInfo= "Send and receive message without keeping your phone online.\n"
    + "Use ChatApp on Up to 4 Linked devices and 1 phone at the same time."
export {
    logoImg,
    defaultInfo,
    dummyUserProfile,
    dummyChatData,
}