
const MessageBox = ({currentChat, message})=>{
    const isMine= currentChat.hostId===message.senderId;
    const sender = currentChat.userList.filter(user=>user.id===message.senderId)[0];
    return (
        <>
            <div
                className={"profile_container"}
                style={{
                    "display": isMine?"none":"inline-block"
                }}
            >
                <img
                    src={sender.profile}
                    alt="user_profile"
                />
                {sender.name}
            </div>

            {message.message}
        </>
    )
}
export default MessageBox;