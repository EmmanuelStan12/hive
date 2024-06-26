import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";

import { checkIsLiked } from "@/lib/utils";
import {
    useLikePost,
    useSavePost,
    useDeleteSavedPost,
    useGetCurrentUser, useGetSavedPosts, useDeleteLikedPost,
} from "@/lib/react-query/queries";
import { IPost, IUser } from "@/types";

type PostStatsProps = {
    post: IPost;
    userId: number;
};

const PostStats = ({ post, userId }: PostStatsProps) => {
    const location = useLocation();
    const likesList = post.likes?.map((user: IUser) => user.id) || [];

    const [likes, setLikes] = useState<number[]>(likesList);
    const [isSaved, setIsSaved] = useState(false);

    const { mutate: likePost } = useLikePost();
    const { mutate: savePost } = useSavePost();
    const { mutate: deleteSavePost } = useDeleteSavedPost();
    const { mutate: deleteLikePost } = useDeleteLikedPost();

    const { data: currentUser } = useGetCurrentUser();
    const { data: savedPosts } = useGetSavedPosts();

    const savedPostRecord = savedPosts?.find(record => record.id === post.id)

    useEffect(() => {
        setIsSaved(!!savedPostRecord);
    }, [currentUser]);

    const handleLikePost = (
        e: React.MouseEvent<HTMLImageElement, MouseEvent>,
    ) => {
        e.stopPropagation();

        let likesArray = [...likes];

        if (likesArray.includes(userId)) {
            likesArray = likesArray.filter((Id) => Id !== userId);
            deleteLikePost(post.id)
        } else {
            likesArray.push(userId);
            likePost({ postId: post.id });
        }

        setLikes(likesArray);
    };

    const handleSavePost = (
        e: React.MouseEvent<HTMLImageElement, MouseEvent>,
    ) => {
        e.stopPropagation();

        if (savedPostRecord) {
            setIsSaved(false);
            return deleteSavePost(savedPostRecord.id);
        }

        savePost({ postId: post.id });
        setIsSaved(true);
    };

    const containerStyles = location.pathname.startsWith("/profile")
        ? "w-full"
        : "";

    return (
        <div
            className={`flex justify-between items-center z-20 ${containerStyles}`}>
            <div className="flex gap-2 mr-5">
                <img
                    src={`${
                        checkIsLiked(likes, userId)
                            ? "/assets/icons/liked.svg"
                            : "/assets/icons/like.svg"
                    }`}
                    alt="like"
                    width={20}
                    height={20}
                    onClick={(e) => handleLikePost(e)}
                    className="cursor-pointer"
                />
                <p className="small-medium lg:base-medium">{likes.length}</p>
            </div>

            <div className="flex gap-2">
                <img
                    src={isSaved ? "/assets/icons/saved.svg" : "/assets/icons/save.svg"}
                    alt="share"
                    width={20}
                    height={20}
                    className="cursor-pointer"
                    onClick={(e) => handleSavePost(e)}
                />
            </div>
        </div>
    );
};

export default PostStats;
