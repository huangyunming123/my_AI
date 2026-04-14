<template>
  <div class="win7-desktop">
    <!-- 桌面图标区域 -->
    <div class="desktop-icons">
      <div 
        v-for="(icon, index) in desktopIcons" 
        :key="index"
        class="desktop-icon"
        :class="{ 'selected': selectedIcon === index }"
        @click="selectIcon(index)"
        @dblclick="openWindow(icon)"
      >
        <div class="icon-image" v-html="icon.svg"></div>
        <div class="icon-label">{{ icon.name }}</div>
      </div>
    </div>

    <!-- 打开的窗口 -->
    <div 
      v-for="(window, index) in openWindows" 
      :key="window.id"
      class="win7-window"
      :style="window.style"
      @mousedown="bringToFront(window.id)"
    >
      <!-- 窗口标题栏 -->
      <div class="window-titlebar" @mousedown="startDrag($event, window)">
        <div class="window-title">
          <span class="title-icon" v-html="window.icon"></span>
          <span>{{ window.title }}</span>
        </div>
        <div class="window-controls">
          <button class="control-btn minimize" @click="minimizeWindow(window.id)">─</button>
          <button class="control-btn maximize" @click="maximizeWindow(window.id)">□</button>
          <button class="control-btn close" @click="closeWindow(window.id)">×</button>
        </div>
      </div>
      
      <!-- 窗口内容 -->
      <div class="window-content">
        <component :is="window.component" v-if="window.component" />
        <div v-else class="default-content">
          <p>{{ window.content }}</p>
        </div>
      </div>
    </div>

    <!-- 任务栏 -->
    <div class="taskbar">
      <!-- 开始按钮 -->
      <button class="start-button" @click="toggleStartMenu">
        <div class="start-logo">
          <svg width="20" height="20" viewBox="0 0 20 20">
            <circle cx="10" cy="10" r="8" fill="#3a6ea5"/>
            <text x="10" y="14" text-anchor="middle" fill="white" font-size="12">W</text>
          </svg>
        </div>
        <span>开始</span>
      </button>

      <!-- 任务栏按钮 -->
      <div class="taskbar-buttons">
        <button 
          v-for="window in openWindows" 
          :key="window.id"
          class="taskbar-btn"
          :class="{ 'active': activeWindowId === window.id }"
          @click="toggleWindow(window.id)"
        >
          <span class="taskbar-icon" v-html="window.icon"></span>
          <span>{{ window.title }}</span>
        </button>
      </div>

      <!-- 系统托盘 -->
      <div class="system-tray">
        <div class="tray-icons">
          <span>🔊</span>
          <span>📶</span>
          <span>🔋</span>
        </div>
        <div class="clock">{{ currentTime }}</div>
      </div>
    </div>

    <!-- 开始菜单 -->
    <div v-if="showStartMenu" class="start-menu" @click.stop>
      <div class="start-menu-header">
        <div class="user-avatar">
          <svg width="40" height="40" viewBox="0 0 40 40">
            <circle cx="20" cy="20" r="18" fill="#4a90d9"/>
            <circle cx="20" cy="15" r="6" fill="white"/>
            <path d="M 8 32 Q 20 24 32 32" stroke="white" stroke-width="3" fill="none"/>
          </svg>
        </div>
        <div class="user-name">用户</div>
      </div>
      <div class="start-menu-programs">
        <div 
          v-for="(program, index) in startMenuPrograms" 
          :key="index"
          class="program-item"
          @click="openProgram(program)"
        >
          <span class="program-icon" v-html="program.icon"></span>
          <span>{{ program.name }}</span>
        </div>
      </div>
      <div class="start-menu-footer">
        <button class="shutdown-btn" @click="shutdown">关机</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Win7Desktop',
  data() {
    return {
      selectedIcon: null,
      openWindows: [],
      activeWindowId: null,
      showStartMenu: false,
      currentTime: '',
      nextWindowId: 1,
      nextWindowPosition: { x: 50, y: 50 },
      draggingWindow: null,
      dragOffset: { x: 0, y: 0 },
      desktopIcons: [
        {
          name: '计算机',
          svg: `<svg width="48" height="48" viewBox="0 0 48 48">
            <rect x="8" y="6" width="32" height="24" rx="2" fill="#4a90d9"/>
            <rect x="10" y="8" width="28" height="18" fill="#1a5490"/>
            <rect x="18" y="32" width="12" height="4" fill="#888"/>
            <rect x="14" y="36" width="20" height="2" rx="1" fill="#666"/>
          </svg>`
        },
        {
          name: '文档',
          svg: `<svg width="48" height="48" viewBox="0 0 48 48">
            <path d="M 8 8 L 24 8 L 32 16 L 32 40 L 8 40 Z" fill="#f4d03f"/>
            <path d="M 24 8 L 24 16 L 32 16" fill="#e6b800"/>
            <rect x="12" y="20" width="16" height="2" fill="#d4ac0d"/>
            <rect x="12" y="26" width="12" height="2" fill="#d4ac0d"/>
            <rect x="12" y="32" width="14" height="2" fill="#d4ac0d"/>
          </svg>`
        },
        {
          name: '网络',
          svg: `<svg width="48" height="48" viewBox="0 0 48 48">
            <circle cx="24" cy="24" r="16" fill="none" stroke="#4a90d9" stroke-width="2"/>
            <circle cx="24" cy="24" r="8" fill="#4a90d9"/>
            <line x1="24" y1="8" x2="24" y2="16" stroke="#4a90d9" stroke-width="2"/>
            <line x1="24" y1="32" x2="24" y2="40" stroke="#4a90d9" stroke-width="2"/>
            <line x1="8" y1="24" x2="16" y2="24" stroke="#4a90d9" stroke-width="2"/>
            <line x1="32" y1="24" x2="40" y2="24" stroke="#4a90d9" stroke-width="2"/>
          </svg>`
        },
        {
          name: '回收站',
          svg: `<svg width="48" height="48" viewBox="0 0 48 48">
            <rect x="12" y="12" width="24" height="28" rx="2" fill="#888"/>
            <rect x="10" y="8" width="28" height="4" rx="1" fill="#666"/>
            <rect x="20" y="4" width="8" height="4" rx="1" fill="#666"/>
            <line x1="18" y1="18" x2="18" y2="34" stroke="#666" stroke-width="2"/>
            <line x1="24" y1="18" x2="24" y2="34" stroke="#666" stroke-width="2"/>
            <line x1="30" y1="18" x2="30" y2="34" stroke="#666" stroke-width="2"/>
          </svg>`
        },
        {
          name: '控制面板',
          svg: `<svg width="48" height="48" viewBox="0 0 48 48">
            <rect x="8" y="8" width="32" height="32" rx="4" fill="#5cb85c"/>
            <circle cx="16" cy="16" r="4" fill="white"/>
            <circle cx="32" cy="16" r="4" fill="white"/>
            <circle cx="16" cy="32" r="4" fill="white"/>
            <circle cx="32" cy="32" r="4" fill="white"/>
            <rect x="22" y="22" width="4" height="4" fill="white"/>
          </svg>`
        }
      ],
      startMenuPrograms: [
        {
          name: '记事本',
          icon: `<svg width="16" height="16" viewBox="0 0 16 16">
            <rect x="2" y="2" width="12" height="12" fill="#f4d03f"/>
            <line x1="4" y1="5" x2="12" y2="5" stroke="#333" stroke-width="1"/>
            <line x1="4" y1="8" x2="12" y2="8" stroke="#333" stroke-width="1"/>
            <line x1="4" y1="11" x2="10" y2="11" stroke="#333" stroke-width="1"/>
          </svg>`,
          component: 'notepad'
        },
        {
          name: '计算器',
          icon: `<svg width="16" height="16" viewBox="0 0 16 16">
            <rect x="2" y="2" width="12" height="12" rx="2" fill="#666"/>
            <rect x="3" y="3" width="10" height="4" fill="#999"/>
            <circle cx="5" cy="10" r="1" fill="white"/>
            <circle cx="8" cy="10" r="1" fill="white"/>
            <circle cx="11" cy="10" r="1" fill="white"/>
            <circle cx="5" cy="13" r="1" fill="white"/>
            <circle cx="8" cy="13" r="1" fill="white"/>
            <circle cx="11" cy="13" r="1" fill="white"/>
          </svg>`,
          component: 'calculator'
        },
        {
          name: '画图',
          icon: `<svg width="16" height="16" viewBox="0 0 16 16">
            <rect x="2" y="2" width="12" height="12" fill="white" stroke="#333" stroke-width="1"/>
            <circle cx="6" cy="6" r="2" fill="red"/>
            <circle cx="10" cy="6" r="2" fill="blue"/>
            <circle cx="8" cy="10" r="2" fill="green"/>
          </svg>`,
          component: 'paint'
        }
      ]
    }
  },
  mounted() {
    this.updateClock()
    setInterval(this.updateClock, 1000)
    document.addEventListener('click', this.handleOutsideClick)
    document.addEventListener('mousemove', this.handleDrag)
    document.addEventListener('mouseup', this.stopDrag)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleOutsideClick)
    document.removeEventListener('mousemove', this.handleDrag)
    document.removeEventListener('mouseup', this.stopDrag)
  },
  methods: {
    updateClock() {
      const now = new Date()
      this.currentTime = now.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    },
    selectIcon(index) {
      this.selectedIcon = index
    },
    openWindow(icon) {
      const id = this.nextWindowId++
      const windowData = {
        id,
        title: icon.name,
        icon: icon.svg,
        content: `这是 ${icon.name} 的内容`,
        component: null,
        style: {
          left: `${this.nextWindowPosition.x}px`,
          top: `${this.nextWindowPosition.y}px`,
          width: '600px',
          height: '400px',
          zIndex: this.openWindows.length + 1
        },
        minimized: false,
        maximized: false
      }
      
      this.openWindows.push(windowData)
      this.activeWindowId = id
      
      // 更新下一个窗口的位置
      this.nextWindowPosition.x += 30
      this.nextWindowPosition.y += 30
      if (this.nextWindowPosition.x > 300) {
        this.nextWindowPosition.x = 50
      }
      if (this.nextWindowPosition.y > 300) {
        this.nextWindowPosition.y = 50
      }
    },
    openProgram(program) {
      this.showStartMenu = false
      const id = this.nextWindowId++
      const windowData = {
        id,
        title: program.name,
        icon: program.icon,
        content: null,
        component: program.component,
        style: {
          left: `${this.nextWindowPosition.x}px`,
          top: `${this.nextWindowPosition.y}px`,
          width: '500px',
          height: '350px',
          zIndex: this.openWindows.length + 1
        },
        minimized: false,
        maximized: false
      }
      
      this.openWindows.push(windowData)
      this.activeWindowId = id
      
      this.nextWindowPosition.x += 30
      this.nextWindowPosition.y += 30
      if (this.nextWindowPosition.x > 300) {
        this.nextWindowPosition.x = 50
      }
      if (this.nextWindowPosition.y > 300) {
        this.nextWindowPosition.y = 50
      }
    },
    closeWindow(id) {
      this.openWindows = this.openWindows.filter(w => w.id !== id)
      if (this.activeWindowId === id) {
        this.activeWindowId = this.openWindows.length > 0 
          ? this.openWindows[this.openWindows.length - 1].id 
          : null
      }
    },
    minimizeWindow(id) {
      const window = this.openWindows.find(w => w.id === id)
      if (window) {
        window.minimized = true
      }
    },
    maximizeWindow(id) {
      const window = this.openWindows.find(w => w.id === id)
      if (window) {
        window.maximized = !window.maximized
        if (window.maximized) {
          window.style.left = '0px'
          window.style.top = '0px'
          window.style.width = '100%'
          window.style.height = 'calc(100% - 40px)'
        } else {
          window.style.left = '50px'
          window.style.top = '50px'
          window.style.width = '600px'
          window.style.height = '400px'
        }
      }
    },
    bringToFront(id) {
      this.activeWindowId = id
      this.openWindows.forEach(window => {
        if (window.id === id) {
          window.style.zIndex = this.openWindows.length + 1
        }
      })
    },
    toggleWindow(id) {
      const window = this.openWindows.find(w => w.id === id)
      if (window) {
        if (window.minimized) {
          window.minimized = false
          this.bringToFront(id)
        } else if (this.activeWindowId === id) {
          window.minimized = true
        } else {
          this.bringToFront(id)
        }
      }
    },
    toggleStartMenu() {
      this.showStartMenu = !this.showStartMenu
    },
    handleOutsideClick(event) {
      if (this.showStartMenu && !event.target.closest('.start-menu') && !event.target.closest('.start-button')) {
        this.showStartMenu = false
      }
    },
    startDrag(event, window) {
      if (event.target.closest('.window-controls')) return
      this.draggingWindow = window
      const rect = event.target.getBoundingClientRect()
      this.dragOffset = {
        x: event.clientX - rect.left,
        y: event.clientY - rect.top
      }
      this.bringToFront(window.id)
    },
    handleDrag(event) {
      if (!this.draggingWindow) return
      const x = event.clientX - this.dragOffset.x
      const y = event.clientY - this.dragOffset.y
      this.draggingWindow.style.left = `${x}px`
      this.draggingWindow.style.top = `${y}px`
    },
    stopDrag() {
      this.draggingWindow = null
    },
    shutdown() {
      if (confirm('确定要关机吗？')) {
        alert('系统已关闭')
        this.openWindows = []
        this.showStartMenu = false
      }
    }
  }
}
</script>

<style scoped>
.win7-desktop {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #1e5799 0%,#207cca 51%,#2989d8 51%,#7db9e8 100%);
  position: relative;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  user-select: none;
}

/* 桌面图标 */
.desktop-icons {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  flex-wrap: wrap;
  height: calc(100vh - 40px);
  align-content: flex-start;
}

.desktop-icon {
  width: 80px;
  text-align: center;
  cursor: pointer;
  padding: 5px;
  border-radius: 3px;
  transition: all 0.2s;
}

.desktop-icon:hover {
  background: rgba(255, 255, 255, 0.2);
}

.desktop-icon.selected {
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.icon-image {
  width: 48px;
  height: 48px;
  margin: 0 auto 5px;
}

.icon-label {
  color: white;
  font-size: 11px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.8);
  word-wrap: break-word;
  max-width: 70px;
  margin: 0 auto;
}

/* 窗口样式 */
.win7-window {
  position: absolute;
  background: #f0f0f0;
  border-radius: 6px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  border: 1px solid #4a90d9;
  overflow: hidden;
  min-width: 300px;
  min-height: 200px;
}

.window-titlebar {
  background: linear-gradient(to bottom, #4a90d9, #357abd);
  color: white;
  padding: 5px 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: move;
  height: 30px;
}

.window-title {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
  font-weight: 500;
}

.title-icon {
  width: 16px;
  height: 16px;
}

.window-controls {
  display: flex;
  gap: 2px;
}

.control-btn {
  width: 24px;
  height: 20px;
  border: none;
  background: transparent;
  color: white;
  font-size: 14px;
  cursor: pointer;
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.control-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.control-btn.close:hover {
  background: #e74c3c;
}

.window-content {
  padding: 10px;
  height: calc(100% - 30px);
  background: white;
  overflow: auto;
}

.default-content {
  padding: 20px;
  text-align: center;
  color: #666;
}

/* 任务栏 */
.taskbar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 40px;
  background: linear-gradient(to bottom, #3a6ea5, #2c5aa0);
  display: flex;
  align-items: center;
  padding: 0 5px;
  z-index: 9999;
  border-top: 1px solid #4a90d9;
}

.start-button {
  display: flex;
  align-items: center;
  gap: 5px;
  background: linear-gradient(to bottom, #5cb85c, #4cae4c);
  color: white;
  border: 1px solid #4cae4c;
  padding: 5px 12px;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  height: 30px;
}

.start-button:hover {
  background: linear-gradient(to bottom, #6bc96b, #5cb85c);
}

.taskbar-buttons {
  flex: 1;
  display: flex;
  gap: 3px;
  margin: 0 10px;
}

.taskbar-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: linear-gradient(to bottom, #4a90d9, #357abd);
  color: white;
  border: 1px solid #2c5aa0;
  padding: 3px 8px;
  border-radius: 2px;
  cursor: pointer;
  font-size: 12px;
  max-width: 150px;
  overflow: hidden;
  white-space: nowrap;
  height: 28px;
}

.taskbar-btn.active {
  background: linear-gradient(to bottom, #2c5aa0, #1e4a80);
  border-color: #1a4070;
}

.taskbar-btn:hover {
  background: linear-gradient(to bottom, #5aa0e9, #4a90d9);
}

.taskbar-icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

.system-tray {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  font-size: 12px;
  padding: 0 10px;
}

.tray-icons {
  display: flex;
  gap: 5px;
}

.clock {
  font-size: 12px;
  min-width: 50px;
  text-align: center;
}

/* 开始菜单 */
.start-menu {
  position: absolute;
  bottom: 40px;
  left: 0;
  width: 300px;
  background: white;
  border: 1px solid #4a90d9;
  border-radius: 5px 5px 0 0;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  z-index: 10000;
  overflow: hidden;
}

.start-menu-header {
  background: linear-gradient(to right, #4a90d9, #357abd);
  color: white;
  padding: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 40px;
  height: 40px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.start-menu-programs {
  padding: 5px 0;
}

.program-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  cursor: pointer;
  font-size: 13px;
}

.program-item:hover {
  background: #e8f4fd;
}

.program-icon {
  width: 16px;
  height: 16px;
}

.start-menu-footer {
  border-top: 1px solid #ddd;
  padding: 8px 10px;
  text-align: right;
}

.shutdown-btn {
  background: linear-gradient(to bottom, #d9534f, #c9302c);
  color: white;
  border: none;
  padding: 5px 15px;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
}

.shutdown-btn:hover {
  background: linear-gradient(to bottom, #e0605c, #d9534f);
}

/* 记事本组件 */
.notepad {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.notepad textarea {
  flex: 1;
  border: none;
  resize: none;
  padding: 5px;
  font-family: Consolas, monospace;
  font-size: 14px;
  outline: none;
}

/* 计算器组件 */
.calculator {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.calc-display {
  background: #222;
  color: #0f0;
  padding: 10px;
  font-size: 20px;
  text-align: right;
  margin-bottom: 10px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

.calc-buttons {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 5px;
  flex: 1;
}

.calc-btn {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  background: linear-gradient(to bottom, #f9f9f9, #e9e9e9);
  cursor: pointer;
  border-radius: 3px;
}

.calc-btn:hover {
  background: linear-gradient(to bottom, #fff, #f0f0f0);
}

.calc-btn.operator {
  background: linear-gradient(to bottom, #4a90d9, #357abd);
  color: white;
}

.calc-btn.equals {
  background: linear-gradient(to bottom, #5cb85c, #4cae4c);
  color: white;
  grid-column: span 2;
}

/* 画图组件 */
.paint {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.paint-toolbar {
  display: flex;
  gap: 5px;
  padding: 5px;
  background: #f0f0f0;
  border-bottom: 1px solid #ccc;
}

.color-btn {
  width: 24px;
  height: 24px;
  border: 2px solid #ccc;
  cursor: pointer;
  border-radius: 2px;
}

.color-btn.active {
  border-color: #333;
}

.paint-canvas {
  flex: 1;
  cursor: crosshair;
  background: white;
}
</style>
