export type INavLink = {
    imgURL: string;
    route: string;
    label: string;
};

export type IUpdateUser = {
    userId: number;
    name: string;
    bio: string;
    file: File[];
};

export type INewPost = {
    caption: string;
    file: File[];
    location?: string;
    tags?: string;
};

export type IUpdatePost = {
    postId: number;
    caption: string;
    file?: File[];
    location?: string;
    tags?: string;
};

export type IPost = {
    id: number;
    caption: string;
    imageId: string;
    imageUrl: URL;
    location?: string;
    tags: string[];
    creator: IUser;
    createdAt: string
    likes: IUser[];
    saves: IUser[];
};

export type IUser = {
    id: number;
    name: string;
    username: string;
    email: string;
    imageUrl: string;
    bio: string;
    posts: IPost[]
    savedPosts?: IPost[]
    likedPosts?: IPost[]
};

export type INewUser = {
    name: string;
    email: string;
    username: string;
    password: string;
};
