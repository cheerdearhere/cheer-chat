import {TbCircleDashed} from "react-icons/tb";
import {BiCommentDetail} from "react-icons/bi";
import {AiOutlineSearch} from "react-icons/ai";
import {BsFilter} from "react-icons/bs";

import {defaultChatData, defaultInfo, dummyChatListData, dummyUserProfile, logoImg} from "../api/dummyData.js";
import ChatCard from "../components/ChatCard.jsx";
import {useEffect, useLayoutEffect, useState} from "react";
import MessageBox from "../components/MessageBox.jsx";

const Home =()=>{
    const [chatList, setChatList] = useState([]);
    const [search, setSearch] = useState("");
    const [filteredChats, setFilteredChats] = useState([]);
    const [currentChat, setCurrentChat] = useState(null);
    const {name, profile} = dummyUserProfile;
    const [chatInfo, setChatInfo] = useState(defaultInfo);


    useLayoutEffect(() => {
        setChatList(dummyChatListData);
    }, []);
    useEffect(()=>{
        setFilteredChats(() => {
            if(search===""||search.trim()==="") return chatList;
            else return chatList.filter(chat=>chat.name.includes(search));
        });
    },[chatList, search]);
    const onSearchKeyword = e=>{
        const query = e.target.value;
        setSearch(query);
    }
    const onSelectChatHandler = /*async*/ (chatId)=> {
        // const selectedChatById = await getChatById(chatId);
        setCurrentChat(defaultChatData);
    }
    return (
        <div className="relative">
            <div className='w-full py-14 bg-[#00a884]'></div>
            <div className="flex bg-[#f0f2f5] h-[94vh] absolute top-6 left-6 w-full">
                <div className="left w-[30%] bg-[#e8e9ec] h-full">
                    {/*left menu bar*/}
                    <div className="w-full">
                        <div className="flex justify-between items-center p-3">
                            {/*account container*/}
                            <div className="flex items-center space-x-3">
                                <img
                                    className="rounded-full w-10 h-10 cursor-pointer"
                                    src={profile}
                                    alt="image_profile"
                                />
                                <p>{name}</p>
                            </div>
                            {/*button container*/}
                            <div className="space-x-3 text-2xl flex">
                                <TbCircleDashed/>
                                <BiCommentDetail/>
                            </div>
                        </div>
                        {/* search input bar */}
                        <div className="relative flex justify-center items-center bg-white py-4 px-3">
                            <input
                                value={search}
                                onChange={onSearchKeyword}
                                className="border-none outline-none bg-slate-200 rounded-md w-[93%] pl-9 py-2"
                                placeholder="search or start new chat"
                                type="text"
                            />
                            <AiOutlineSearch className="left-6 top-7 absolute"/>
                            <div>
                                <BsFilter className="ml-4 text-3xl"/>
                            </div>
                        </div>
                        {/* all user */}
                        <div className="bg-white overflow-y-scroll h-[76.8vh] px-3">
                            {/*{name, profile,message, timestamp,notice}*/}
                            {filteredChats?.map(chat=>{
                                return (
                                    <div
                                        key={`chat${chat.id}`}
                                        onClick={()=>{
                                            onSelectChatHandler(chat.id);
                                        }}
                                    >
                                        <ChatCard
                                            {...chat}
                                        />
                                    </div>
                                )
                            })}
                        </div>
                    </div>
                </div>
                {
                    currentChat ? (
                        //message part
                        <div>
                            {currentChat?.messages
                                ?  (
                                    <div>
                                        {currentChat.messages.map(m=>{
                                            return (
                                                <div key={`msg${m.messageId}`}>
                                                    <MessageBox
                                                        message={m}
                                                        currentChat={currentChat}
                                                    />
                                                </div>
                                            )
                                        })}
                                    </div>
                                    )
                                :   (
                                    <div>
                                        no chat data
                                    </div>
                                )
                            }
                        </div>
                    ) : (
                      //default
                    <div className={"w-[70%] flex flex-col items-center justify-center h-full"}>
                        <div className="max-w-[70%] text-center">
                            <img
                                className="max-w-[70%] max-h-[70%]"
                                src={logoImg}
                                alt="userImg"
                            />
                            <h1 className="text-4xl text-gray-600">Cheer App</h1>
                            <p className="my-9">
                                {defaultInfo}
                            </p>
                        </div>
                    </div>
                    )
                }
            </div>
        </div>
    );
}
export default Home;