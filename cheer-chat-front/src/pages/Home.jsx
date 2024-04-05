import {TbCircleDashed} from "react-icons/tb";
import {BiCommentDetail} from "react-icons/bi";

const Home =()=>{
    return (
        <div className="relative">
            <div className='w-full py-14 bg-[#00a884]'></div>
            <div className="flex bg-[#f0f2f5] h-[94vh] absolute top-6 left-6 w-full">
                <div className="left w-[30%] bg-[#e8e9ec] h-full">
                    {/*left menu bar*/}
                    <div className="w-full">
                        <div className="flex justify-between items-center p-3">
                            <div className="flex items-center space-x-3">
                                {/*account container*/}
                                <img
                                    className="rounded-full w-10 h-10 cursor-pointer"
                                    src="https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg"
                                    alt="profile image"
                                />
                                <p>username</p>
                            </div>
                            <div className="space-x-3 text-2xl flex">
                                {/*button container*/}
                                <TbCircleDashed/>
                                <BiCommentDetail/>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="right">

                </div>
            </div>
        </div>
    );
}
export default Home;